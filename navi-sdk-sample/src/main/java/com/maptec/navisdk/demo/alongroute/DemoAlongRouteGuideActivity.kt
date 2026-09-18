package com.maptec.navisdk.demo.alongroute

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.maptec.navi.sdk.NaviSdk
import com.maptec.navi.sdk.api.GuideController
import com.maptec.navi.sdk.guide.model.GuideScreenState
import com.maptec.navi.sdk.standalone.NaviSdkGuideActivity
import kotlinx.coroutines.launch

/**
 * Demo：继承 [NaviSdkGuideActivity]，在导航页槽里塞沿途搜入口与输入面板。
 *
 * 由 [com.maptec.navi.sdk.showGuideActivity] 的 `activityClass` 唤起，勿走全局 Provider。
 * 沿途搜走 [NaviSdk.search]，不注入 Biz。
 */
class DemoAlongRouteGuideActivity : NaviSdkGuideActivity() {

    private val showPanel = mutableStateOf(false)
    private val searchPois = mutableStateOf<List<DemoAlongRoutePoi>>(emptyList())

    @Composable
    override fun topEndOverlay(topPadding: Dp, controller: GuideController) {
        val screenState by controller.guideScreenState.collectAsState()
        if (screenState == GuideScreenState.Finish ||
            screenState == GuideScreenState.Preparing ||
            screenState == GuideScreenState.PrepareFailed
        ) {
            return
        }
        DemoAlongRouteEntryButton(
            onClick = ::toggleAlongRoutePanel,
            modifier = Modifier.padding(end = 16.dp, top = topPadding),
        )
    }

    @Composable
    override fun contentOverlay(controller: GuideController) {
        val pois by searchPois
        DemoAlongRouteMarkers(pois)

        if (!showPanel.value) return
        Box(
            modifier = Modifier
                .fillMaxSize()
                .imePadding(),
            contentAlignment = Alignment.BottomCenter,
        ) {
            DemoAlongRouteSearchPanel(
                onBack = ::dismissAlongRoutePanel,
                onSearch = { query ->
                    dismissAlongRoutePanel()
                    searchAlongRoute(controller, query)
                },
                onCategoryClick = { category ->
                    dismissAlongRoutePanel()
                    searchAlongRoute(controller, category.query)
                },
            )
        }
    }

    private fun toggleAlongRoutePanel() {
        searchPois.value = emptyList()
        showPanel.value = !showPanel.value
    }

    private fun dismissAlongRoutePanel() {
        showPanel.value = false
    }

    private fun searchAlongRoute(controller: GuideController, query: String) {
        val polyline = controller.remainingRoutePolyline()
        if (polyline.isNullOrEmpty()) {
            Log.w(TAG, "alongRouteSearch skip: no encoded polyline")
            return
        }
        lifecycleScope.launch {
            NaviSdk.search.alongRouteSearch(
                query = query,
                encodedPolyline = polyline,
                resultLimit = 10,
            )
                .onSuccess { response ->
                    val pois = response.results.orEmpty().mapNotNull { item ->
                        val loc = item.place.location ?: return@mapNotNull null
                        DemoAlongRoutePoi(
                            name = item.place.displayName?.text ?: item.place.name,
                            latitude = loc.latitude,
                            longitude = loc.longitude,
                        )
                    }
                    searchPois.value = pois
                    if (pois.isNotEmpty()) {
                        controller.updateScreenState(GuideScreenState.Overview)
                    }
                    Log.i(TAG, "alongRouteSearch query=$query status=${response.status} count=${pois.size}")
                }
                .onFailure { error ->
                    searchPois.value = emptyList()
                    Log.w(TAG, "alongRouteSearch failed: ${error.message}", error)
                }
        }
    }

    private companion object {
        const val TAG = "DemoAlongRoute"
    }
}
