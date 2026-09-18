package com.maptec.navisdk.demo.menu

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.maptec.navi.sdk.api.GuideUiVisibility
import com.maptec.navisdk.demo.R

private val NavMethodOptions = listOf(
    MenuPickerOption(NavMethod.Real, R.string.demo_menu_item_nav_method_real),
    MenuPickerOption(NavMethod.Simulated, R.string.demo_menu_item_nav_method_simulated),
)

/** 导航组件：Switch + 导航方式，跳转时带模拟开关与 [GuideUiVisibility]。 */
@Composable
fun NavComponentCard(
    onEnter: (simulatedNavigation: Boolean, GuideUiVisibility) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    var visibility by remember { mutableStateOf(GuideUiVisibility()) }
    var selectedMethod by remember { mutableStateOf(NavMethod.Real) }

    ExpandableConfigSection(
        titleRes = R.string.demo_menu_section_nav_component,
        expanded = expanded,
        onToggle = { expanded = !expanded },
        modifier = modifier,
    ) {
        SwitchSettingRow(
            labelRes = R.string.demo_menu_switch_guide_card,
            checked = visibility.maneuverPanel,
            onCheckedChange = { visibility = visibility.copy(maneuverPanel = it) },
        )
        SwitchSettingRow(
            labelRes = R.string.demo_menu_switch_lane,
            checked = visibility.maneuverLaneGuidance,
            onCheckedChange = { visibility = visibility.copy(maneuverLaneGuidance = it) },
        )
        SwitchSettingRow(
            labelRes = R.string.demo_menu_switch_speedometer,
            checked = visibility.speedDisplay,
            onCheckedChange = { visibility = visibility.copy(speedDisplay = it) },
        )
        SwitchSettingRow(
            labelRes = R.string.demo_menu_switch_guide_arrow,
            checked = visibility.mapOverlayManeuverArrow,
            onCheckedChange = { visibility = visibility.copy(mapOverlayManeuverArrow = it) },
        )
        SwitchSettingRow(
            labelRes = R.string.demo_menu_switch_road_name,
            checked = visibility.curRouteName,
            onCheckedChange = { visibility = visibility.copy(curRouteName = it) },
        )
        SwitchSettingRow(
            labelRes = R.string.demo_menu_switch_eta_card,
            checked = visibility.bottomEtaCard,
            onCheckedChange = { visibility = visibility.copy(bottomEtaCard = it) },
        )
        SwitchSettingRow(
            labelRes = R.string.demo_menu_switch_exit,
            checked = visibility.exitNavButton,
            onCheckedChange = { visibility = visibility.copy(exitNavButton = it) },
        )
        SwitchSettingRow(
            labelRes = R.string.demo_menu_switch_overview,
            checked = visibility.overviewButton,
            onCheckedChange = { visibility = visibility.copy(overviewButton = it) },
        )
        SwitchSettingRow(
            labelRes = R.string.demo_menu_switch_recenter,
            checked = visibility.recenterButton,
            onCheckedChange = { visibility = visibility.copy(recenterButton = it) },
        )
        SwitchSettingRow(
            labelRes = R.string.demo_menu_switch_eta_bar,
            checked = visibility.etaLightBar,
            onCheckedChange = { visibility = visibility.copy(etaLightBar = it) },
        )
        SwitchSettingRow(
            labelRes = R.string.demo_menu_switch_play_style,
            checked = visibility.playStyle,
            onCheckedChange = { visibility = visibility.copy(playStyle = it) },
        )
        SwitchSettingRow(
            labelRes = R.string.demo_menu_switch_refresh,
            checked = visibility.refreshButton,
            onCheckedChange = { visibility = visibility.copy(refreshButton = it) },
        )
        MenuPickerSettingRow(
            labelRes = R.string.demo_menu_item_nav_method,
            selected = selectedMethod,
            options = NavMethodOptions,
            onSelected = { selectedMethod = it },
        )
        PrimaryActionButton(
            textRes = R.string.demo_menu_btn_enter_nav_component,
            onClick = {
                onEnter(
                    selectedMethod == NavMethod.Simulated,
                    visibility,
                )
            },
        )
    }
}
