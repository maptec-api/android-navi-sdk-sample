package com.maptec.navisdk.demo.menu

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maptec.navisdk.demo.R


internal object MenuColors {
    val pageBackground = Color.White
    val textPrimary = Color(0xFF0F172A)
    val cardSubtitleSecondary = Color(0xFF727272)
    val subItem = Color(0xFF1E293B)
    val stroke = Color(0xFFE2E8F0)
    val expandedHeaderBg = Color(0xFFF8FAFC)
    val chevron = Color(0xFF94A3B8)
    val primaryButtonBg = Color(0xFF148DF9)
    val primaryButtonText = Color.White
    val valueText = Color(0xFFBDC1C6)
    val valueLinkText = Color.Black
    val switchThumb = Color.White
    val cardShadowTint = Color(0x0D0F172A)
    val clickDisabled = Color(0xFFF2F2F2)
}

internal object MenuShapes {
    val card = RoundedCornerShape(16.dp)
    val button = RoundedCornerShape(12.dp)
}

internal val MenuCardShadow = Modifier.shadow(
    elevation = 8.dp,
    shape = MenuShapes.card,
    ambientColor = MenuColors.cardShadowTint,
    spotColor = MenuColors.cardShadowTint,
)

internal fun Modifier.menuCardSurface(
    background: Color,
    onClick: (() -> Unit)? = null,
): Modifier {
    var modifier = this
        .then(MenuCardShadow)
        .clip(MenuShapes.card)
        .background(background)
        .border(width = 1.dp, color = MenuColors.stroke, shape = MenuShapes.card)
    if (onClick != null) {
        modifier = modifier.clickable(onClick = onClick)
    }
    return modifier
}

@Composable
internal fun MenuExpandChevron(
    expanded: Boolean,
    size: Dp,
    modifier: Modifier = Modifier,
) {
    Icon(
        imageVector = if (expanded) {
            Icons.Filled.KeyboardArrowDown
        } else {
            Icons.AutoMirrored.Filled.KeyboardArrowRight
        },
        contentDescription = stringResource(
            if (expanded) {
                R.string.demo_menu_a11y_collapse
            } else {
                R.string.demo_menu_a11y_expand
            },
        ),
        tint = MenuColors.chevron,
        modifier = modifier.size(size),
    )
}

@Composable
internal fun MenuSettingRow(
    @StringRes labelRes: Int,
    modifier: Modifier = Modifier,
    showDivider: Boolean = true,
    trailing: @Composable () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(labelRes),
                color = MenuColors.subItem,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
            )
            trailing()
        }
        if (showDivider) {
            HorizontalDivider(color = MenuColors.stroke, thickness = 1.dp)
        }
    }
}

@Composable
internal fun SimpleCategoryCard(
    @StringRes titleRes: Int,
    @StringRes subtitleRes: Int?,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(58.dp)
            .menuCardSurface(
                background = if (onClick !=null ) MenuColors.pageBackground else MenuColors.clickDisabled,
                onClick = onClick)
            .padding(horizontal = 18.dp, vertical = 9.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = if (subtitleRes != null) {
                    Arrangement.spacedBy(3.dp)
                } else {
                    Arrangement.Top
                },
            ) {
                Text(
                    text = stringResource(titleRes),
                    color = MenuColors.textPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 22.sp,
                )
                if (subtitleRes != null) {
                    Text(
                        text = stringResource(subtitleRes),
                        color = MenuColors.cardSubtitleSecondary,
                        fontSize = 11.sp,
                        lineHeight = 15.sp,
                    )
                }
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = stringResource(R.string.demo_menu_a11y_navigate),
                tint = MenuColors.chevron,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

/** 独立灰色 Header 卡片 + 下方居中内容区（非单张包裹卡片） */
@Composable
internal fun ExpandableConfigSection(
    @StringRes titleRes: Int,
    expanded: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .menuCardSurface(background = MenuColors.expandedHeaderBg, onClick = onToggle)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(titleRes),
                modifier = Modifier.weight(1f),
                color = MenuColors.textPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 22.sp,
            )
            MenuExpandChevron(expanded = expanded, size = 20.dp)
        }

        if (expanded) {
            Column(
                modifier = Modifier
                    .widthIn(max = 282.dp)
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                content()
            }
        }
    }
}

@Composable
internal fun SwitchSettingRow(
    @StringRes labelRes: Int,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    MenuSettingRow(labelRes = labelRes) {
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.size(width = 48.dp, height = 28.dp),
            colors = SwitchDefaults.colors(
                checkedTrackColor = MenuColors.primaryButtonBg,
                uncheckedTrackColor = MenuColors.stroke,
                checkedThumbColor = MenuColors.switchThumb,
                uncheckedThumbColor = MenuColors.switchThumb,
                uncheckedBorderColor = MenuColors.stroke,
            ),
        )
    }
}

@Composable
internal fun CollapsibleSubSection(
    @StringRes titleRes: Int,
    expanded: Boolean,
    onToggle: () -> Unit,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onToggle),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(titleRes),
                color = MenuColors.subItem,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
            )
            MenuExpandChevron(expanded = expanded, size = 14.dp)
        }
        HorizontalDivider(color = MenuColors.stroke, thickness = 1.dp)
        if (expanded) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                content()
            }
        }
    }
}

@Composable
internal fun GroupLabelRow(
    @StringRes labelRes: Int,
) {
    Text(
        text = stringResource(labelRes),
        color = MenuColors.subItem,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
internal fun ValueFieldRow(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val fieldInteractionSource = remember { MutableInteractionSource() }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            color = MenuColors.subItem,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .height(36.dp)
                .padding(horizontal = 8.dp, vertical = 6.dp)
                .clickable(
                    indication = null,
                    interactionSource = fieldInteractionSource,
                ) {
                    focusRequester.requestFocus()
                    keyboardController?.show()
                },
            contentAlignment = Alignment.CenterEnd,
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = keyboardType,
                    imeAction = ImeAction.Next,
                ),
                textStyle = TextStyle(
                    color = MenuColors.valueText,
                    fontSize = 12.sp,
                    lineHeight = 14.sp,
                    textAlign = TextAlign.End,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
            )
        }
    }
}

@Composable
internal fun ValueLinkSettingRow(
    @StringRes labelRes: Int,
    @StringRes valueRes: Int,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(labelRes),
            color = MenuColors.subItem,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = stringResource(valueRes),
                color = MenuColors.valueLinkText,
                fontSize = 12.sp,
                lineHeight = 14.sp,
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = stringResource(R.string.demo_menu_a11y_navigate),
                tint = MenuColors.chevron,
                modifier = Modifier.size(14.dp),
            )
        }
    }
}

@Composable
internal fun LinkSettingRow(
    @StringRes labelRes: Int,
    onClick: () -> Unit,
) {
    MenuSettingRow(
        labelRes = labelRes,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = stringResource(R.string.demo_menu_a11y_navigate),
            tint = MenuColors.chevron,
            modifier = Modifier.size(14.dp),
        )
    }
}

data class MenuPickerOption<T>(
    val value: T,
    @StringRes val labelRes: Int,
)
@Composable
internal fun <T> MenuPickerSettingRow(
    @StringRes labelRes: Int,
    selected: T,
    options: List<MenuPickerOption<T>>,
    onSelected: (T) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedLabelRes = options.first { it.value == selected }.labelRes

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(labelRes),
            color = MenuColors.subItem,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
        )
        Box {
            Row(
                modifier = Modifier.clickable { expanded = true },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = stringResource(selectedLabelRes),
                    color = MenuColors.valueLinkText,
                    fontSize = 12.sp,
                    lineHeight = 14.sp,
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = stringResource(R.string.demo_menu_a11y_navigate),
                    tint = MenuColors.chevron,
                    modifier = Modifier.size(14.dp),
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = stringResource(option.labelRes),
                                fontSize = 14.sp,
                            )
                        },
                        onClick = {
                            expanded = false
                            onSelected(option.value)
                        },
                    )
                }
            }
        }
    }
}

@Composable
internal fun PrimaryActionButton(
    @StringRes textRes: Int,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(32.dp)
            .clip(MenuShapes.button)
            .background(MenuColors.primaryButtonBg)
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(textRes),
            color = MenuColors.primaryButtonText,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 20.sp,
        )
    }
}
