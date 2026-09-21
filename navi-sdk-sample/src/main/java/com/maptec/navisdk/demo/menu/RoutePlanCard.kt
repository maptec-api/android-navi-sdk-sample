package com.maptec.navisdk.demo.menu

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.maptec.navi.sdk.api.RoutePlanUiVisibility
import com.maptec.navisdk.demo.NaviSdkDemoScenario
import com.maptec.navisdk.demo.R

private val RoutePlanMethodOptions = listOf(
    MenuPickerOption(NaviSdkDemoScenario.ROUTE_START_END, R.string.demo_menu_route_method_start_end),
    MenuPickerOption(NaviSdkDemoScenario.ROUTE_NO_START, R.string.demo_menu_route_method_no_start),
    MenuPickerOption(NaviSdkDemoScenario.ROUTE_WITH_VIAS, R.string.demo_menu_route_method_with_vias),
)

/** 行前规划组件：Switch + 算路方式，跳转时带 [RoutePlanUiVisibility]。 */
@Composable
fun RoutePlanCard(
    onEnter: (NaviSdkDemoScenario, RoutePlanUiVisibility) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    var visibility by remember { mutableStateOf(RoutePlanUiVisibility()) }
    var selectedScenario by remember { mutableStateOf(NaviSdkDemoScenario.ROUTE_START_END) }

    ExpandableConfigSection(
        titleRes = R.string.demo_menu_section_route_plan_component,
        expanded = expanded,
        onToggle = { expanded = !expanded },
        modifier = modifier,
    ) {
        SwitchSettingRow(
            labelRes = R.string.demo_menu_switch_route_refresh,
            checked = visibility.routeRefreshButton,
            onCheckedChange = { visibility = visibility.copy(routeRefreshButton = it) },
        )
        SwitchSettingRow(
            labelRes = R.string.demo_menu_switch_route_preference,
            checked = visibility.routeOptions,
            onCheckedChange = { visibility = visibility.copy(routeOptions = it) },
        )
        SwitchSettingRow(
            labelRes = R.string.demo_menu_switch_route_detail,
            checked = visibility.routeDetailsEntry,
            onCheckedChange = { visibility = visibility.copy(routeDetailsEntry = it) },
        )
        SwitchSettingRow(
            labelRes = R.string.demo_menu_switch_route_card,
            checked = visibility.routeResultPanel,
            onCheckedChange = { visibility = visibility.copy(routeResultPanel = it) },
        )
        SwitchSettingRow(
            labelRes = R.string.demo_menu_switch_start_navi,
            checked = visibility.startNaviButton,
            onCheckedChange = { visibility = visibility.copy(startNaviButton = it) },
        )
        MenuPickerSettingRow(
            labelRes = R.string.demo_menu_item_route_method,
            selected = selectedScenario,
            options = RoutePlanMethodOptions,
            onSelected = { selectedScenario = it },
        )
        PrimaryActionButton(
            textRes = R.string.demo_menu_btn_enter_route_plan,
            onClick = { onEnter(selectedScenario, visibility) },
        )
    }
}
