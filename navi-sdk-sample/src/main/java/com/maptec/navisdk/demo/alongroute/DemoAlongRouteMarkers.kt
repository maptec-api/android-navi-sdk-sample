package com.maptec.navisdk.demo.alongroute

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.maptec.applied.geometry.LatLng
import com.maptec.applied.maps.overlay.marker.Marker
import com.maptec.mapengine.api.model.LocalMapController
import com.maptec.mapengine.api.model.MapMarkerOptions

/**
 * 将沿途搜 POI 画到当前底图；离开组合或结果变化时 [com.maptec.mapengine.api.MapController.removeMarker]，
 * 不调用 clearMarkers，避免清掉导航路线点。
 */
@Composable
internal fun DemoAlongRouteMarkers(pois: List<DemoAlongRoutePoi>) {
    val mapController = LocalMapController.current
    val styleEpoch by mapController.mapStyleEpoch.collectAsState()
    DisposableEffect(pois, styleEpoch) {
        val added = mutableListOf<Marker>()
        pois.forEach { poi ->
            mapController.addMarker(
                MapMarkerOptions(
                    latLng = LatLng(poi.latitude, poi.longitude),
                    text = poi.name,
                    draggable = false,
                ),
            )?.let(added::add)
        }
        onDispose {
            added.forEach(mapController::removeMarker)
        }
    }
}
