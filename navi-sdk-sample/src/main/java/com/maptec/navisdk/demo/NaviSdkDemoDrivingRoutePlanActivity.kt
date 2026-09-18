package com.maptec.navisdk.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.maptec.applied.android.route.callback.RouteCalculateCallback
import com.maptec.applied.android.route.data.RetMessage
import com.maptec.applied.android.route.data.RouteRequest
import com.maptec.applied.android.route.data.RouteResult
import com.maptec.navi.sdk.NaviSdkHostAccess
import com.maptec.navi.sdk.biz.NaviRouteBizService
import com.maptec.navi.sdk.ui.NaviSdkMapScaffold
import com.maptec.navi.sdk.ui.NaviSdkRoot
import com.maptec.navi.sdk.api.NaviSdkConfig
import kotlinx.coroutines.launch

/**
 * 路径规划 API 测试页：调用 [NaviRouteBizService] 发起驾车算路并在地图上绘制。
 */
class NaviSdkDemoDrivingRoutePlanActivity : ComponentActivity() {

    private val naviRouteBizService: NaviRouteBizService
        get() = NaviSdkHostAccess.naviRouteBizService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            NaviSdkRoot(config = NaviSdkConfig()) {
                DrivingRoutePlanScreen(
                    onCalculate = { onResult ->
                        lifecycleScope.launch {
                            naviRouteBizService.calculateRoute(
                                NaviSdkDemoSamples.drivingRouteRequest(),
                                object : RouteCalculateCallback {
                                    override fun onRouteResult(
                                        ret: RetMessage,
                                        jobId: Long,
                                        req: RouteRequest,
                                        result: RouteResult,
                                    ) {
                                        onResult(ret, result)
                                    }
                                },
                            )
                        }
                    },
                )
            }
        }
    }
}

@Composable
private fun DrivingRoutePlanScreen(
    onCalculate: (onResult: (RetMessage, RouteResult) -> Unit) -> Unit,
) {
    var statusText by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        statusText = "算路中…"
        onCalculate { ret, result ->
            statusText = if (ret.ret == 0) {
                val pathCount = result.path.size
                "算路成功，共 $pathCount 条路线"
            } else {
                "算路失败：${ret.ret}"
            }
        }
    }

    NaviSdkMapScaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
        ) {
            Text(
                text = stringResource(R.string.demo_item_driving),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xCCFFFFFF))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                fontSize = 16.sp,
                color = Color(0xFF111111),
            )
            if (statusText.isNotEmpty()) {
                Text(
                    text = statusText,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xCCFFFFFF))
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    fontSize = 14.sp,
                    color = Color(0xFF333333),
                )
            }
        }
    }
}
