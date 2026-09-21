package com.maptec.navisdk.demo.menu

import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.maptec.navi.sdk.api.GuideUiColors
import com.maptec.navisdk.demo.R
import androidx.core.graphics.toColorInt
import com.maptec.navi.sdk.api.GuideScalePitchAngle

private const val DefaultPanelDayHex = "#0051AD"
private const val DefaultPanelNightHex = "#003B87"
private const val DefaultLaneDayHex = "#003B87"
private const val DefaultLaneNightHex = "#0051AD"
private const val DefaultHeadUpZoom = 17
private const val DefaultHeadUpPitch = 30
private const val DefaultNorthUpZoom = 15
private const val DefaultNorthUpPitch = 0

private fun String.toArgbOrDefault(fallbackHex: String): Int =
    runCatching { toColorInt() }.getOrElse { fallbackHex.toColorInt() }

private fun String.capAtMax(max: Int): String {
    val n = toDoubleOrNull() ?: return this
    return if (n > max) max.toString() else this
}

private fun String.toDoubleOrDefault(default: Int): Double =
    toDoubleOrNull() ?: default.toDouble()

/** 组件自定义配置：折叠子分组，CTA 进入导航。 */
@Composable
fun UiCustomCard(
    onEnterManeuverColor: (GuideUiColors) -> Unit,
    onEnterPerspective: (GuideScalePitchAngle) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current

    var expanded by remember { mutableStateOf(false) }
    var colorGroupExpanded by remember { mutableStateOf(true) }
    var fixedViewGroupExpanded by remember { mutableStateOf(true) }

    var maneuverInfoDay by remember { mutableStateOf(DefaultPanelDayHex) }
    var maneuverInfoNight by remember { mutableStateOf(DefaultPanelNightHex) }
    var maneuverInfoLaneDay by remember { mutableStateOf(DefaultLaneDayHex) }
    var maneuverInfoLaneNight by remember { mutableStateOf(DefaultLaneNightHex) }

    var headUpZoom by remember { mutableStateOf(DefaultHeadUpZoom.toString()) }
    var headUpPitch by remember { mutableStateOf(DefaultHeadUpPitch.toString()) }
    var northUpZoom by remember { mutableStateOf(DefaultNorthUpZoom.toString()) }
    var northUpPitch by remember { mutableStateOf(DefaultNorthUpPitch.toString()) }


    ExpandableConfigSection(
        titleRes = R.string.demo_menu_section_ui_custom,
        expanded = expanded,
        onToggle = { expanded = !expanded },
        modifier = modifier,
    ) {
        CollapsibleSubSection(
            titleRes = R.string.demo_menu_item_custom_guide_color,
            expanded = colorGroupExpanded,
            onToggle = { colorGroupExpanded = !colorGroupExpanded },
        ) {
            // 自定义引导面板颜色
            GroupLabelRow(labelRes = R.string.demo_menu_custom_day_mode)
            ValueFieldRow(
                label = stringResource( R.string.demo_menu_custom_main_panel_color),
                value = maneuverInfoDay,
                onValueChange = { maneuverInfoDay = it },
            )
            ValueFieldRow(
                label = stringResource( R.string.demo_menu_custom_lane_color),
                value = maneuverInfoLaneDay,
                onValueChange = { maneuverInfoLaneDay = it },
            )
            HorizontalDivider(color = MenuColors.stroke, thickness = 1.dp)
            GroupLabelRow(labelRes = R.string.demo_menu_custom_night_mode)
            ValueFieldRow(
                label = stringResource( R.string.demo_menu_custom_main_panel_color),
                value = maneuverInfoNight,
                onValueChange = { maneuverInfoNight = it },
            )
            ValueFieldRow(
                label = stringResource( R.string.demo_menu_custom_lane_color),
                value = maneuverInfoLaneNight,
                onValueChange = { maneuverInfoLaneNight = it },
            )
            HorizontalDivider(color = MenuColors.stroke, thickness = 1.dp)
            PrimaryActionButton(
                textRes = R.string.demo_menu_btn_enter_nav_component,
                onClick = {
                    focusManager.clearFocus()
                    onEnterManeuverColor(
                        GuideUiColors(
                            maneuverPanelDayArgb = maneuverInfoDay.toArgbOrDefault(DefaultPanelDayHex),
                            maneuverPanelNightArgb = maneuverInfoNight.toArgbOrDefault(DefaultPanelNightHex),
                            laneStripDayArgb = maneuverInfoLaneDay.toArgbOrDefault(DefaultLaneDayHex),
                            laneStripNightArgb = maneuverInfoLaneNight.toArgbOrDefault(DefaultLaneNightHex),
                        )
                    )
                },
            )
            HorizontalDivider(color = MenuColors.stroke, thickness = 1.dp)
        }
        CollapsibleSubSection(
            titleRes = R.string.demo_menu_item_custom_fixed_view,
            expanded = fixedViewGroupExpanded,
            onToggle = { fixedViewGroupExpanded = !fixedViewGroupExpanded },
        ) {
            // 自定义固定视角比例尺与俯仰角
            GroupLabelRow(labelRes = R.string.demo_menu_custom_heading_up)
            ValueFieldRow(
                label = stringResource(  R.string.demo_menu_custom_scale),
                value = headUpZoom,
                onValueChange = { headUpZoom = it.capAtMax(DefaultHeadUpZoom) },
                keyboardType = KeyboardType.Decimal,
            )
            ValueFieldRow(
                label = stringResource( R.string.demo_menu_custom_pitch),
                value = headUpPitch,
                onValueChange = { headUpPitch = it.capAtMax(DefaultNorthUpPitch) },
                keyboardType = KeyboardType.Decimal,
            )
            HorizontalDivider(color = MenuColors.stroke, thickness = 1.dp)
            GroupLabelRow(labelRes = R.string.demo_menu_custom_north_up)
            ValueFieldRow(
                label = stringResource( R.string.demo_menu_custom_scale),
                value = northUpZoom,
                onValueChange = { northUpZoom = it.capAtMax(DefaultHeadUpZoom) },
                keyboardType = KeyboardType.Decimal,
            )
            ValueFieldRow(
                label = stringResource( R.string.demo_menu_custom_pitch),
                value = northUpPitch,
                onValueChange = { northUpPitch = it.capAtMax(DefaultNorthUpPitch) },
                keyboardType = KeyboardType.Decimal,
            )
            HorizontalDivider(color = MenuColors.stroke, thickness = 1.dp)
            PrimaryActionButton(
                textRes = R.string.demo_menu_btn_enter_nav_component,
                onClick = {
                    focusManager.clearFocus()
                    onEnterPerspective(
                        GuideScalePitchAngle(
                            headUpZoom = headUpZoom.toDoubleOrDefault(DefaultHeadUpZoom),
                            headUpPitch = headUpPitch.toDoubleOrDefault(DefaultHeadUpPitch),
                            northUpZoom = northUpZoom.toDoubleOrDefault(DefaultNorthUpZoom),
                            northUpPitch = northUpPitch.toDoubleOrDefault(DefaultNorthUpPitch),
                        )
                    )
                },
            )
            HorizontalDivider(color = MenuColors.stroke, thickness = 1.dp)
        }
    }
}
