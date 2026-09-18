package com.maptec.navisdk.demo

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.maptec.navi.sdk.api.NaviSdkPageLaunchReason
import com.maptec.navi.sdk.api.NaviSdkPageLaunchResult

/**
 * Demo：宿主侧处理 [NaviSdkPageLaunchResult] 的参考实现。
 *
 * - 成功：打日志，返回 true，调用方可继续业务。
 * - 失败：按 [NaviSdkPageLaunchReason] 映射本地化文案并 Toast；**不**启动 Activity。
 *
 * 接入方可直接复用或替换为 Snackbar / Dialog 等 UI。
 */
object NaviSdkPageLaunchResultHandler {

    private const val TAG = "NaviSdkDemo"

    /**
     * @return `true` 表示 [NaviSdkPageLaunchResult.Success]；`false` 表示校验失败，页面未唤起。
     */
    fun handle(context: Context, result: NaviSdkPageLaunchResult): Boolean =
        when (result) {
            NaviSdkPageLaunchResult.Success -> {
                Log.i(TAG, "page launch: Success")
                true
            }
            is NaviSdkPageLaunchResult.Failure -> {
                val message = localizedMessage(context, result)
                Log.w(
                    TAG,
                    "page launch: Failure reason=${result.reason} viaIndex=${result.viaPointIndex} sdkMessage=${result.message}",
                )
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                false
            }
        }

    /** 将 SDK 英文 [NaviSdkPageLaunchResult.Failure.message] 映射为 Demo 本地化说明。 */
    fun localizedMessage(context: Context, failure: NaviSdkPageLaunchResult.Failure): String =
        when (failure.reason) {
            NaviSdkPageLaunchReason.NOT_INITIALIZED ->
                context.getString(R.string.demo_page_launch_not_initialized)
            NaviSdkPageLaunchReason.INVALID_END_POI ->
                context.getString(R.string.demo_page_launch_invalid_end)
            NaviSdkPageLaunchReason.INVALID_START_POI ->
                context.getString(R.string.demo_page_launch_invalid_start)
            NaviSdkPageLaunchReason.INVALID_VIA_POI -> {
                val index = failure.viaPointIndex ?: -1
                context.getString(R.string.demo_page_launch_invalid_via, index)
            }
        }
}
