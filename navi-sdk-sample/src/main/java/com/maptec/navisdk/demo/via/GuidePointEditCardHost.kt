package com.maptec.navisdk.demo.via

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.maptec.applied.android.route.data.Coordinates
import com.maptec.applied.geometry.LatLng
import com.maptec.applied.maps.overlay.marker.Marker
import com.maptec.applied.search.model.response.Place
import com.maptec.mapengine.api.MapController
import com.maptec.mapengine.api.geocode.toPlaceOrNull
import com.maptec.mapengine.api.model.LocalMapController
import com.maptec.mapengine.api.model.MapMarkerOptions
import com.maptec.mapengine.api.viewmodel.GeocodeViewModel
import com.maptec.mapengine.api.viewmodel.rememberGeocodeViewModel
import com.maptec.navi.sdk.R
import com.maptec.navi.sdk.api.GuideController
import com.maptec.navi.sdk.api.GuideRerouteEditAction
import com.maptec.navi.sdk.api.NaviPoi
import com.maptec.navi.sdk.guide.model.GuideScreenState

/**
 * 底部卡片 UI 状态；由本 Host 持有，[GuideController] 只发原子事件（点击/长按/改线成功）。
 *
 * - [RemoveVia]：点击地图途经点 marker，展示删除卡片
 * - [MapPick]：长按地图逆地理成功后，展示添途经点 / 改终点卡片
 */
private sealed interface GuidePointCardState {
    data class RemoveVia(
        val poi: NaviPoi,
        val index: Int,
    ) : GuidePointCardState

    data class MapPick(
        val place: Place,
        val poi: NaviPoi,
        /** 长按选点在地图上打的 pin；[clear] 或 Host 销毁时需移除 */
        val marker: Marker?,
    ) : GuidePointCardState
}

/**
 * 导航中途径点编辑统一 Host。
 *
 * 合并原 [RouteViaPointCard] / [PointInfoCard] 两套 Host，用 [GuidePointCardState] 互斥，
 * 避免两个全屏 Box 叠层。发送端仍走 [GuideController.viaClickedPoint]、
 * [GuideController.mapPickPoint]、[GuideController.rerouteEditSuccess]，本组件只做 collect → 映射 → 渲染。
 */
@Composable
fun GuidePointEditCardHost(
    controller: GuideController,
    modifier: Modifier = Modifier,
    geocodeViewModel: GeocodeViewModel = rememberGeocodeViewModel(),
    /** 用户主动改线重算成功后回调（偏航等自动重算不会 emit [GuideController.rerouteEditSuccess]） */
    onRerouteEditSuccess: () -> Unit = {},
) {
    val mapController: MapController = LocalMapController.current

    var cardState by remember { mutableStateOf<GuidePointCardState?>(null) }

    val guideScreenState by controller.guideScreenState.collectAsState()
    val latestGuideState = rememberUpdatedState(guideScreenState)

    val visible = cardState != null && guideScreenState != GuideScreenState.ExitConfirm

    fun clearMapPickMarker() {
        (cardState as? GuidePointCardState.MapPick)?.marker?.let(mapController::removeMarker)
    }

    /** 关卡片并清理地图选点 marker；新事件进入前先调用以保证互斥 */
    fun clear() {
        clearMapPickMarker()
        cardState = null
    }

    // 1. 点击途经点 marker → 删除卡片（后触发覆盖 MapPick）
    LaunchedEffect(controller) {
        controller.viaClickedPoint.collect { (poi, index) ->
            if (latestGuideState.value == GuideScreenState.ExitConfirm) return@collect
            clear()
            cardState = GuidePointCardState.RemoveVia(poi, index)
        }
    }

    // 2. 长按地图 → 逆地理（先 clear，不立刻改 cardState，等 geocode 结果）
    LaunchedEffect(controller) {
        controller.mapPickPoint.collect { latLng ->
            if (latestGuideState.value == GuideScreenState.ExitConfirm) return@collect
            clear()
            geocodeViewModel.doReverseGeocode(latLng)
        }
    }

    // 3. 逆地理结果 → 地图选点卡片 + 打 marker
    val mapPickFallbackName = stringResource(R.string.map_pick_def_display_name)
    LaunchedEffect(geocodeViewModel) {
        geocodeViewModel.geocodeResults.collect { results ->
            if (results.isEmpty()) return@collect
            val place = results.first().toPlaceOrNull(
                mapPickFallbackName,
                geocodeViewModel.placeSource.value,
            ) ?: return@collect

            geocodeViewModel.clearModeAndResults()
            val location = place.location ?: return@collect

            clearMapPickMarker()
            val latLng = LatLng(location.latitude, location.longitude)
            val marker = mapController.addMarker(MapMarkerOptions(latLng))
            cardState = GuidePointCardState.MapPick(
                place = place,
                poi = NaviPoi(
                    name = place.displayName?.text?:mapPickFallbackName,
                    latitude =  location.latitude,
                    longitude = location.longitude,
                    poiId = place.id),
                marker = marker,
            )
        }
    }

    // 4. 用户改线重算成功 → 关卡片（AddVia / RemoveVia / ChangeDest 均走此 Flow）
    LaunchedEffect(controller) {
        controller.rerouteEditSuccess.collect {
            clear()
            onRerouteEditSuccess()
        }
    }

    // 5. 退出确认等全屏态 → 隐藏兜底
    LaunchedEffect(guideScreenState) {
        if (guideScreenState == GuideScreenState.ExitConfirm) clear()
    }

    // 6. Host 离开 composition → 移除 marker，避免泄漏
    DisposableEffect(Unit) {
        onDispose { clear() }
    }

    if (!visible) return

    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        contentAlignment = Alignment.BottomCenter,
    ) {
        when (val state = cardState) {
            is GuidePointCardState.RemoveVia -> RouteViaPointCard(
                title = state.poi.name,
                modifier = modifier,
                onCloseClick = ::clear,
                onRemoveStopClick = { controller.removeSelectedViaPoint(state.index) },
            )

            is GuidePointCardState.MapPick -> PointInfoCard(
                modifier = modifier,
                title = state.place.displayName?.text?:mapPickFallbackName,
                onCloseClick = ::clear,
                onDirectionsClick = { controller.changeDest(state.poi) },
                onAddStopClick = {
                    controller.addSelectedViaPoint(
                        NaviPoi(
                            name = state.place.displayName?.text?:mapPickFallbackName,
                            latitude = state.poi.latitude,
                            longitude = state.poi.longitude,
                            poiId = state.place.id,
                        ),
                    )
                },
            )

            null -> Unit
        }
    }
}
