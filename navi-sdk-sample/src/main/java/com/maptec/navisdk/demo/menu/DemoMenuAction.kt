package com.maptec.navisdk.demo.menu

import com.maptec.navi.sdk.api.GuideScalePitchAngle
import com.maptec.navi.sdk.api.GuideUiColors
import com.maptec.navi.sdk.api.GuideUiVisibility
import com.maptec.navi.sdk.api.NaviSdkBroadcastLanguage
import com.maptec.navi.sdk.api.RoutePlanUiVisibility
import com.maptec.navisdk.demo.NaviSdkDemoScenario


/** 导航方式：真实 / 模拟。 */
enum class NavMethod {
    Real,
    Simulated,
}

/**
 * 菜单各模块对外跳转事件。模块在点击时带齐参数，Activity 按类型分发。
 */
sealed interface DemoMenuAction {
    data class OpenRoutePlan(
        val scenario: NaviSdkDemoScenario,
        val routeUiVisibility: RoutePlanUiVisibility,
    ) : DemoMenuAction

    data class OpenGuide(
        val simulatedNavigation: Boolean,
        val guideUiVisibility: GuideUiVisibility,
    ) : DemoMenuAction

    data object OpenAlongRoute : DemoMenuAction

    data object OpenViaPointEdit : DemoMenuAction

    data class OpenGuideWithLanguage(val language: NaviSdkBroadcastLanguage) : DemoMenuAction

    data class OpenGuideWithColor(val guideUiColors: GuideUiColors) : DemoMenuAction
    data class OpenGuideWithPerspective(val guideScalePitchAngle: GuideScalePitchAngle): DemoMenuAction
    data object ComingSoon : DemoMenuAction
    data object Auth : DemoMenuAction
}
