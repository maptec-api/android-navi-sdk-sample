package com.maptec.navisdk.demo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.maptec.navi.sdk.api.GuideLaunch
import com.maptec.navi.sdk.api.NaviSdkCallbacks
import com.maptec.navi.sdk.api.NaviSdkEntry
import com.maptec.navi.sdk.api.RoutePlanConfig
import com.maptec.navi.sdk.standalone.NaviSdkNavHost

/**
 * Compose 嵌入 [NaviSdkNavHost] 的测试页。
 *
 * - 算路场景：[NaviSdkEntry.RoutePlan]
 * - 组件直接导航：[GuideLaunch.fromPois] → [NaviSdkEntry.GuideFromPois]（组件内算路）
 */
class NaviSdkDemoNavHostActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val scenario = intent.readScenario()
        val routePlanConfig = intent.readRoutePlanConfig()
            ?: runCatching { NaviSdkDemoSamples.routePlanConfig(scenario) }.getOrNull()

        // 组件直接导航：导航模式与 NaviGuideParams / GuideLaunch 默认一致（经 Config 传入）
        val entry: NaviSdkEntry = when (scenario) {
            NaviSdkDemoScenario.ROUTE_GUIDE_DIRECT -> {
                val params = NaviSdkDemoSamples.guideParamsStartEnd()
                GuideLaunch.fromPois(
                    start = params.start,
                    end = params.end,
                    viaPoints = params.viaPoints,
                    routePreference = params.routePreference,
                    avoidHighway = params.avoidHighway,
                    simulatedNavigation = params.simulatedNavigation,
                    autoScaleEnabled = params.autoScaleEnabled,
                    confirmExit = params.confirmExit,
                    playStyle = params.playStyle,
                    autoResumeFollowOnGesture = params.autoResumeFollowOnGesture,
                )
            }
            else -> NaviSdkEntry.RoutePlan(
                routePlanConfig ?: error("Missing route plan config for $scenario"),
            )
        }

        setContent {
            NaviSdkNavHost(
                entry = entry,
                callbacks = NaviSdkCallbacks(onExit = { finish() }),
            )
        }
    }

    companion object {
        private const val EXTRA_SCENARIO = "demo_scenario"
        private const val EXTRA_ROUTE_PLAN_CONFIG = "demo_route_plan_config"

        fun createIntent(
            context: Context,
            scenario: NaviSdkDemoScenario,
            routePlanConfig: RoutePlanConfig? = null,
        ): Intent = Intent(context, NaviSdkDemoNavHostActivity::class.java).apply {
            putExtra(EXTRA_SCENARIO, scenario.name)
            routePlanConfig?.let { putExtra(EXTRA_ROUTE_PLAN_CONFIG, it) }
        }

        private fun Intent.readScenario(): NaviSdkDemoScenario {
            val name = getStringExtra(EXTRA_SCENARIO)
                ?: error("Missing $EXTRA_SCENARIO")
            return NaviSdkDemoScenario.valueOf(name)
        }

        private fun Intent.readRoutePlanConfig(): RoutePlanConfig? =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                getSerializableExtra(EXTRA_ROUTE_PLAN_CONFIG, RoutePlanConfig::class.java)
            } else {
                @Suppress("DEPRECATION")
                getSerializableExtra(EXTRA_ROUTE_PLAN_CONFIG) as? RoutePlanConfig
            }
    }
}
