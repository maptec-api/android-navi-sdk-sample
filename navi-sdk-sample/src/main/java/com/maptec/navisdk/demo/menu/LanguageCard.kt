package com.maptec.navisdk.demo.menu

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.maptec.navi.sdk.api.NaviSdkBroadcastLanguage
import com.maptec.navisdk.demo.R

private val LanguageOptions = listOf(
    MenuPickerOption(NaviSdkBroadcastLanguage.ZH, R.string.demo_menu_value_language_zh_cn),
    MenuPickerOption(NaviSdkBroadcastLanguage.EN, R.string.demo_menu_value_language_en_us),
)

/** 多语言设置：选择播报语言并对外抛出。 */
@Composable
fun LanguageCard(
    modifier: Modifier = Modifier,
    onEnter: (NaviSdkBroadcastLanguage) -> Unit,

    ) {
    var expanded by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf(NaviSdkBroadcastLanguage.ZH) }

    ExpandableConfigSection(
        titleRes = R.string.demo_menu_section_language_settings,
        expanded = expanded,
        onToggle = { expanded = !expanded },
        modifier = modifier,
    ) {
        MenuPickerSettingRow(
            labelRes = R.string.demo_menu_item_announcement_language,
            selected = selected,
            options = LanguageOptions,
            onSelected = {
                selected = it
            },
        )

        PrimaryActionButton(
            textRes = R.string.demo_menu_btn_enter_nav_component,
            onClick = { onEnter(selected) },
        )
    }
}
