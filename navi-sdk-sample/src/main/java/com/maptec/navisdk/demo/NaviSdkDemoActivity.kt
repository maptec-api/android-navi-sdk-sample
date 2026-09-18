package com.maptec.navisdk.demo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.maptec.navi.sdk.NaviSdk
import com.maptec.navi.sdk.api.GuideScalePitchAngle
import com.maptec.navi.sdk.api.GuideUiColors
import com.maptec.navi.sdk.api.NaviSdkBroadcastLanguage
import com.maptec.navi.sdk.api.NaviSdkUiVisibility
import com.maptec.navi.sdk.showGuideActivity
import com.maptec.navi.sdk.showRouteActivity
import com.maptec.navi.sdk.ui.permission.NaviSdkHostPermissionEffect
import com.maptec.navisdk.demo.alongroute.DemoAlongRouteGuideActivity
import com.maptec.navisdk.demo.via.DemoGuidePointEditActivity
import com.maptec.navisdk.demo.menu.DemoMenuAction
import com.maptec.navisdk.demo.menu.DemoMenuScreen

class NaviSdkDemoActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            NaviSdkHostPermissionEffect()
            DemoMenuScreen(
                onAction = { action -> handleMenuAction(action) },
            )
        }
    }

    /** 菜单 typed Action 分发：各模块已带齐跳转参数。 */
    private fun handleMenuAction(action: DemoMenuAction) {
        when (action) {
            is DemoMenuAction.Auth, DemoMenuAction.ComingSoon -> {
                Toast.makeText(this, R.string.demo_toast_coming_soon, Toast.LENGTH_SHORT).show()
            }
            is DemoMenuAction.OpenRoutePlan -> {
                applyDemoConfig(uiVisibility = NaviSdkUiVisibility(routePlan = action.routeUiVisibility))
                showRoutePlanActivity(action.scenario)
            }
            is DemoMenuAction.OpenGuide -> {
                applyDemoConfig(uiVisibility = NaviSdkUiVisibility(guide = action.guideUiVisibility))
                showGuideActivity(simulatedNavigation = action.simulatedNavigation)
            }
            is DemoMenuAction.OpenAlongRoute -> {
                applyDemoConfig()
                showAlongRouteGuide()
            }
            is DemoMenuAction.OpenViaPointEdit -> {
                applyDemoConfig()
                showGuideViaPointEdit()
            }
            is DemoMenuAction.OpenGuideWithLanguage->{
                applyDemoConfig(broadcastLanguage = action.language)
                showGuideActivity()
            }
            is DemoMenuAction.OpenGuideWithColor -> {
                applyDemoConfig(guideColors = action.guideUiColors)
                showGuideActivity(simulatedNavigation = false)
            }
            is DemoMenuAction.OpenGuideWithPerspective -> {
                applyDemoConfig(guideScalePitchAngle = action.guideScalePitchAngle)
                showGuideActivity(simulatedNavigation = false, autoScaleEnabled = false)
            }
        }
    }

    /** 写入本次菜单项，其余菜单可改项回到默认；theme / mapZoomRange 保留。 */
    fun applyDemoConfig(
        broadcastLanguage: NaviSdkBroadcastLanguage = NaviSdkBroadcastLanguage.SYSTEM,
        uiVisibility: NaviSdkUiVisibility = NaviSdkUiVisibility(),
        guideColors: GuideUiColors = GuideUiColors(),
        guideScalePitchAngle: GuideScalePitchAngle = GuideScalePitchAngle()
    ) {
        val base = NaviSdk.getConfig()
        NaviSdk.updateConfig(
            base.copy(
                broadcastLanguage = broadcastLanguage,
                uiVisibility = uiVisibility,
                guideColors = guideColors,
                guideScalePitchAngle = guideScalePitchAngle,
            ),
        )
    }
    private fun showRoutePlanActivity(scenario: NaviSdkDemoScenario) {
        NaviSdkPageLaunchResultHandler.handle(
            this,
            NaviSdk.showRouteActivity(this, NaviSdkDemoSamples.routePlanParams(scenario)),
        )
    }

    private fun showGuideActivity(
        simulatedNavigation: Boolean = true,
        autoScaleEnabled: Boolean = true,
    ) {
        NaviSdkPageLaunchResultHandler.handle(
            this,
            NaviSdk.showGuideActivity(this,
                NaviSdkDemoSamples.guideParamsStartEnd(simulatedNavigation).copy(
                    autoScaleEnabled = autoScaleEnabled,
                ),
            ),
        )
    }

    private fun showAlongRouteGuide() {
        NaviSdkPageLaunchResultHandler.handle(
            this,
            NaviSdk.showGuideActivity(
                this,
                NaviSdkDemoSamples.guideParamsStartEnd(),
                DemoAlongRouteGuideActivity::class.java,
            ),
        )
    }
    private fun showGuideViaPointEdit() {
        NaviSdkPageLaunchResultHandler.handle(
            this,
            NaviSdk.showGuideActivity(
                this,
                NaviSdkDemoSamples.guideParamsWithVias(),
                DemoGuidePointEditActivity::class.java,
            ),
        )
    }
}
