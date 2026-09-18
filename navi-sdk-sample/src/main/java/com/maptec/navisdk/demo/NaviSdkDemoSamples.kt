package com.maptec.navisdk.demo

import com.maptec.applied.android.route.data.RouteRequest
import com.maptec.applied.geometry.LatLng
import com.maptec.navi.sdk.api.NaviGuideParams
import com.maptec.navi.sdk.api.NaviPoi
import com.maptec.navi.sdk.api.NaviRoutePlanParams
import com.maptec.navi.sdk.api.NaviRoutePreference
import com.maptec.navi.sdk.api.RoutePlanConfig
import com.maptec.applied.android.route.data.Coordinates as RouteCoordinates

/** Demo 用新加坡示例 POI，供各入口场景复用。 */
object NaviSdkDemoSamples {

    private val p1 = LatLng(1.3040, 103.8318) // Orchard
    private val p2 = LatLng(1.3007, 103.8554) // Bugis
    private val p3 = LatLng(1.2839, 103.8515) // Raffles Place
    private val p4 = LatLng(1.2837, 103.8593) // Marina Bay Sands
    private val p5 = LatLng(1.4370, 103.7861) // Woodlands

    val startPoi = NaviPoi("Woodlands", p5)
    val endPoi = NaviPoi("Marina Bay Sands", p4)
    val viaPois = listOf(
        NaviPoi("Orchard", p1),
        NaviPoi("Bugis", p2),
        NaviPoi("Raffles Place", p3),
    )

    fun routePlanConfig(scenario: NaviSdkDemoScenario): RoutePlanConfig = when (scenario) {
        NaviSdkDemoScenario.ROUTE_START_END -> RoutePlanConfig(
            start = startPoi,
            end = endPoi,
        )
        NaviSdkDemoScenario.ROUTE_NO_START -> RoutePlanConfig(
            end = endPoi,
        )
        NaviSdkDemoScenario.ROUTE_WITH_VIAS -> RoutePlanConfig(
            start = startPoi,
            viaPoints = viaPois,
            end = endPoi,
        )
        NaviSdkDemoScenario.ROUTE_WITH_PREFERENCE -> routePlanConfigWithPreference()
        else -> error("Not a route plan scenario: $scenario")
    }

    /** Standalone 入口：起终点算路。 */
    fun routePlanParamsStartEnd(): NaviRoutePlanParams = NaviRoutePlanParams(
        start = startPoi,
        end = endPoi,
    )

    /** Standalone 入口：仅传终点，起点取当前定位。 */
    fun routePlanParamsEndOnly(): NaviRoutePlanParams = NaviRoutePlanParams(
        end = endPoi,
    )

    /** Standalone 入口：含途径点算路。 */
    fun routePlanParamsWithVias(): NaviRoutePlanParams = NaviRoutePlanParams(
        start = startPoi,
        viaPoints = viaPois,
        end = endPoi,
    )

    fun routePlanParams(scenario: NaviSdkDemoScenario): NaviRoutePlanParams = when (scenario) {
        NaviSdkDemoScenario.ROUTE_START_END -> routePlanParamsStartEnd()
        NaviSdkDemoScenario.ROUTE_NO_START -> routePlanParamsEndOnly()
        NaviSdkDemoScenario.ROUTE_WITH_VIAS -> routePlanParamsWithVias()
        else -> error("Not a showRouteActivity route plan scenario: $scenario")
    }

    /** Standalone 入口：传入算路偏好（时间短 + 避开高速）。 */
    fun routePlanParamsWithPreference(): NaviRoutePlanParams = NaviRoutePlanParams(
        start = startPoi,
        end = endPoi,
        routePreference = NaviRoutePreference.FAST,
        avoidHighway = true,
    )

    /** 直接导航：起终点。 */
    fun guideParamsStartEnd(isSimulatedNavigation: Boolean = true): NaviGuideParams = NaviGuideParams(
        start = startPoi,
        end = endPoi,
        simulatedNavigation = isSimulatedNavigation,
    )

    /** 直接导航：仅终点，起点取当前定位。 */
    fun guideParamsEndOnly(): NaviGuideParams = NaviGuideParams(
        end = endPoi,
    )

    /** 直接导航：含途经点。 */
    fun guideParamsWithVias(isSimulatedNavigation: Boolean = true): NaviGuideParams = NaviGuideParams(
        start = startPoi,
        viaPoints = viaPois,
        end = endPoi,
        simulatedNavigation = isSimulatedNavigation
    )

    fun guideParams(scenario: NaviSdkDemoScenario): NaviGuideParams = when (scenario) {
        NaviSdkDemoScenario.GUIDE_START_END,
        NaviSdkDemoScenario.GUIDE_SHOW_ACTIVITY,
        NaviSdkDemoScenario.ROUTE_GUIDE_DIRECT -> guideParamsStartEnd()
        NaviSdkDemoScenario.GUIDE_NO_START -> guideParamsEndOnly()
        NaviSdkDemoScenario.GUIDE_WITH_VIAS -> guideParamsWithVias()
        else -> error("Not a showGuideActivity scenario: $scenario")
    }

    /** Standalone 导航入口默认样例（起终点）。 */
    fun guideParams(): NaviGuideParams = guideParamsStartEnd()

    /** 非法终点坐标，用于演示 [com.maptec.navi.sdk.api.NaviSdkPageLaunchResult.Failure]。 */
    fun routePlanParamsInvalidEnd(): NaviRoutePlanParams = NaviRoutePlanParams(
        start = startPoi,
        end = NaviPoi("Invalid end", 91.0, 103.8593),
    )

    /** NavHost 嵌入：通过 [RoutePlanConfig] 传入算路偏好。 */
    fun routePlanConfigWithPreference(): RoutePlanConfig = RoutePlanConfig(
        start = startPoi,
        end = endPoi,
        routePreference = NaviRoutePreference.SHORT_DISTANCE,
        avoidHighway = true,
    )

    /** 驾车路径规划 API 测试用请求（起终点，无途径点）。 */
    fun drivingRouteRequest(): RouteRequest = RouteRequest(
        startPoint = startPoi.toRouteCoordinates(),
        endPoint = endPoi.toRouteCoordinates(),
    )

    private fun NaviPoi.toRouteCoordinates(): RouteCoordinates =
        RouteCoordinates(latitude, longitude)
}
