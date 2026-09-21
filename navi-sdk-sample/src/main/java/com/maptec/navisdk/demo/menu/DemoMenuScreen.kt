package com.maptec.navisdk.demo.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maptec.navisdk.demo.R

/**
 * 菜单主屏：只做壳，7 个模块各自持有状态并通过 [onAction] 抛出 typed 事件。
 */

@Composable
fun DemoMenuScreen(
    onAction: (DemoMenuAction) -> Unit,
    modifier: Modifier = Modifier,
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MenuColors.pageBackground)
            .statusBarsPadding()
            .padding(start = 26.dp, end = 26.dp, bottom = 26.dp),
    ) {
        Text(
            text = stringResource(R.string.demo_menu_screen_title),
            modifier = Modifier
                .padding(bottom = 20.dp),
            color = MenuColors.textPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 28.sp,
        )

        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
//                SimpleCategoryCard(
//                    titleRes = R.string.demo_card_auth_title,
//                    subtitleRes = R.string.demo_card_auth_subtitle,
//                    onClick = { onAction(DemoMenuAction.Auth) },
//                )
                SimpleCategoryCard(
                    titleRes = R.string.demo_card_quick_start_title,
                    subtitleRes = R.string.demo_card_quick_start_subtitle,
                    //onAction(DemoMenuAction.ComingSoon) },
                )
                RoutePlanCard(
                    onEnter = { scenario, visibility ->
                        onAction(DemoMenuAction.OpenRoutePlan(scenario, visibility))
                    },
                )
                NavComponentCard(
                    onEnter = { simulated, visibility ->
                        onAction(DemoMenuAction.OpenGuide(simulated, visibility))
                    },
                )
                UiCustomCard(
                    onEnterManeuverColor = { onAction(DemoMenuAction.OpenGuideWithColor(it)) },
                    onEnterPerspective = { onAction(DemoMenuAction.OpenGuideWithPerspective(it)) },
                )
                LanguageCard(
                    onEnter = { language ->
                        onAction(DemoMenuAction.OpenGuideWithLanguage(language))
                    },
                )
                ExtensionCard(
                    onAlongRoute = {
                        onAction(DemoMenuAction.OpenAlongRoute)
                    },
                    onViaPointEdit = {
                        onAction(DemoMenuAction.OpenViaPointEdit)
                    },
                )
        }
    }
}
