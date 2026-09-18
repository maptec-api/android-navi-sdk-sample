package com.maptec.navisdk.demo.alongroute

import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maptec.mapengine.api.theme.LightExtendedColors
import com.maptec.navisdk.demo.R

/** 沿途搜 POI，供 Marker 绘制。 */
internal data class DemoAlongRoutePoi(
    val name: String,
    val latitude: Double,
    val longitude: Double,
)

internal enum class DemoAlongRouteCategory(
    @StringRes val labelRes: Int,
    @DrawableRes val iconRes: Int,
    val gradientColors: List<Color>,
    val query: String,
) {
    GAS_STATION(
        labelRes = R.string.demo_along_route_category_gas,
        iconRes = R.drawable.ic_demo_category_gas,
        gradientColors = listOf(Color(0xFF4B77EF), Color(0xFF7FB4FF)),
        query = "station",
    ),
    CHARGING_STATION(
        labelRes = R.string.demo_along_route_category_charging,
        iconRes = R.drawable.ic_demo_category_charging,
        gradientColors = listOf(Color(0xFF4B77EF), Color(0xFF7FB4FF)),
        query = "EV_Charging_Station",
    ),
    RESTROOM(
        labelRes = R.string.demo_along_route_category_restroom,
        iconRes = R.drawable.ic_demo_category_restroom,
        gradientColors = listOf(Color(0xFFFF9D34), Color(0xFFFDC03F)),
        query = "Public_Toilet",
    ),
    DINING(
        labelRes = R.string.demo_along_route_category_dining,
        iconRes = R.drawable.ic_demo_category_dining,
        gradientColors = listOf(Color(0xFFFF604C), Color(0xFFFF9B36)),
        query = "Restaurant",
    ),
}

/**
 * 沿途搜输入面板（Figma 4216-3353）：搜索栏 + 分类快捷入口，不展示结果列表。
 */
@Composable
internal fun DemoAlongRouteSearchPanel(
    onBack: () -> Unit,
    onSearch: (String) -> Unit,
    onCategoryClick: (DemoAlongRouteCategory) -> Unit,
    modifier: Modifier = Modifier,
) {
    var query by remember { mutableStateOf("") }
    // 设计稿固定浅色面板，不随导航夜间地图主题切换
    val colors = LightExtendedColors

    BackHandler(onBack = onBack)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .background(colors.BackgroundDefaultDefault),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .clip(RoundedCornerShape(999.dp))
                    .background(colors.BackgroundNeutralDefaul),
                contentAlignment = Alignment.CenterStart,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.size(40.dp),
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_demo_arrow_back),
                            contentDescription = stringResource(R.string.demo_along_route_back),
                            modifier = Modifier.size(20.dp),
                            tint = colors.TextDefaultPrimary,
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        if (query.isEmpty()) {
                            Text(
                                text = stringResource(R.string.demo_along_route_search_hint),
                                fontSize = 16.sp,
                                lineHeight = 24.sp,
                                color = colors.TextDefaultTertiary,
                            )
                        }
                        BasicTextField(
                            value = query,
                            onValueChange = { query = it },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(
                                onSearch = {
                                    val keyword = query.trim()
                                    if (keyword.isNotEmpty()) onSearch(keyword)
                                },
                            ),
                            textStyle = TextStyle(
                                fontSize = 16.sp,
                                lineHeight = 24.sp,
                                color = colors.TextDefaultPrimary,
                            ),
                        )
                    }

                    Text(
                        text = stringResource(R.string.demo_along_route_search),
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .clickable {
                                val keyword = query.trim()
                                if (keyword.isNotEmpty()) onSearch(keyword)
                            },
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        fontWeight = FontWeight.Medium,
                        color = colors.TextLinkDefault,
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            DemoAlongRouteCategory.entries.forEach { category ->
                DemoAlongRouteCategoryItem(
                    category = category,
                    onClick = { onCategoryClick(category) },
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .height(8.dp)
                .background(colors.BackgroundDefaultSecondary),
        )
    }
}

@Composable
private fun DemoAlongRouteCategoryItem(
    category: DemoAlongRouteCategory,
    onClick: () -> Unit,
) {
    val colors = LightExtendedColors
    Column(
        modifier = Modifier
            .padding(vertical = 12.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        colors = category.gradientColors,
                    ),
                ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(category.iconRes),
                contentDescription = stringResource(category.labelRes),
                modifier = Modifier.size(24.dp),
                tint = Color.White,
            )
        }
        Text(
            text = stringResource(category.labelRes),
            modifier = Modifier.padding(top = 4.dp),
            fontSize = 10.sp,
            lineHeight = 14.sp,
            color = colors.TextDefaultPrimary,
        )
    }
}
