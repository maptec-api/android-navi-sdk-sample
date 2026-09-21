package com.maptec.navisdk.demo

/**
 * Demo 菜单场景：决定启动哪个 SDK 入口 / 带哪些入参。
 * 供菜单、Activity、NavHost、Samples 等复用。
 */
enum class NaviSdkDemoScenario {
    /** 行前规划：showRouteActivity(start/end) */
    ROUTE_START_END,
    /** 行前规划：showRouteActivity(end)，起点由当前定位 */
    ROUTE_NO_START,
    /** 行前规划：showRouteActivity 含 viaPoints */
    ROUTE_WITH_VIAS,
    /** 直接导航：showGuideActivity(start/end) */
    GUIDE_START_END,
    /** 直接导航：showGuideActivity(end only)，起点由当前定位 */
    GUIDE_NO_START,
    /** 直接导航：showGuideActivity 含 viaPoints */
    GUIDE_WITH_VIAS,
    /** 传入算路偏好（NavHost）：RoutePlanConfig.routePreference / avoidHighway */
    ROUTE_WITH_PREFERENCE,
    /** 组件直接导航：GuideLaunch.fromPois → NaviSdkEntry.GuideFromPois（默认模拟导航） */
    ROUTE_GUIDE_DIRECT,
    /** 传入算路偏好（showRouteActivity）：NaviSdk.showRouteActivity(NaviRoutePlanParams) */
    ROUTE_CUSTOM_ACTIVITY,
    /** 传入起终点导航：NaviSdk.showGuideActivity(NaviGuideParams) */
    GUIDE_SHOW_ACTIVITY,
    /** 子类 NaviSdkGuideActivity：导航页槽展示沿途搜 */
    GUIDE_ALONG_ROUTE,
    /** 驾车路径规划：自定义页直接调 NaviRouteBizService 算路画线 */
    ROUTE_DRIVING,
    /** showRouteActivity + 非法终点，演示 Failure 处理 */
    PAGE_LAUNCH_INVALID_END,
    /** 驾车路径引导：预留 */
    GUIDE_DRIVING,
    /** 业务层接口演示：NaviRouteBizService（预留） */
    BIZ_ROUTE_SERVICE,
    /** 原子层接口演示：NaviRouteService（预留） */
    ATOMIC_ROUTE_SERVICE,

    /** 子类 NaviSdkGuideActivity：导航页 途径点 删除 */
    GUIDE_VIA_POINT_EDIT,
}
