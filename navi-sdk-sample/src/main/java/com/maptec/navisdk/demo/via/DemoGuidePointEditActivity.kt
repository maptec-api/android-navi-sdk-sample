package com.maptec.navisdk.demo.via

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import com.maptec.navi.sdk.api.GuideController
import com.maptec.navi.sdk.guide.model.GuideScreenState
import com.maptec.navi.sdk.standalone.NaviSdkGuideActivity

/**
 * Demo：继承 [NaviSdkGuideActivity]，在导航页槽里塞途径点编辑半屏面板（删/添途经点、改终点）。
 *
 * 由 [com.maptec.navi.sdk.showGuideActivity] 的 `activityClass` 唤起，勿走全局 Provider。
 */
class DemoGuidePointEditActivity() : NaviSdkGuideActivity() {

    @Composable
    override fun contentOverlay(controller: GuideController) {
        GuidePointEditCardHost(
            modifier = Modifier.zIndex(11f),
            controller = controller,
            onRerouteEditSuccess = {
                controller.updateScreenState(GuideScreenState.Overview)
            },
        )
    }
}
