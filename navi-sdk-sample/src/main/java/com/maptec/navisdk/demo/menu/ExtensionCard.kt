package com.maptec.navisdk.demo.menu

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.maptec.navi.sdk.api.GuideUiVisibility
import com.maptec.navisdk.demo.R

/** 拓展功能：沿途搜 / 途径点变更，跳转时带默认 [GuideUiVisibility] 快照。 */
@Composable
fun ExtensionCard(
    onAlongRoute: () -> Unit,
    onViaPointEdit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    ExpandableConfigSection(
        titleRes = R.string.demo_menu_section_extension,
        expanded = expanded,
        onToggle = { expanded = !expanded },
        modifier = modifier,
    ) {
        LinkSettingRow(
            labelRes = R.string.demo_menu_item_along_route,
            onClick = { onAlongRoute() },
        )
        LinkSettingRow(
            labelRes = R.string.demo_menu_item_via_point_edit,
            onClick = { onViaPointEdit() },
        )
    }
}
