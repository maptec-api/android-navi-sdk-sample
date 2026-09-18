package com.maptec.navisdk.demo

import android.app.Application
import android.util.Log
import com.maptec.navi.sdk.NaviSdk
import com.maptec.navi.sdk.api.NaviSdkConfig
import com.maptec.navi.sdk.api.NaviSdkBusinessEvent
import com.maptec.navi.sdk.api.NaviSdkInitResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class NaviSdkDemoApplication : Application() {

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    override fun onCreate() {
        super.onCreate()
        when (val result = NaviSdk.initialize(this, NaviSdkConfig())) {
            is NaviSdkInitResult.Success ->
                Log.i(TAG, "NaviSdk initialize success")
            is NaviSdkInitResult.Failure ->
                Log.e(
                    TAG,
                    "NaviSdk initialize failed: stage=${result.stage}, message=${result.message}",
                    result.cause,
                )
        }
        appScope.launch {
            NaviSdk.businessEvents.collect { event ->
                when (event) {
                    is NaviSdkBusinessEvent.Arrived ->
                        Log.i(TAG, "event: Arrived")
                    is NaviSdkBusinessEvent.GuideExit ->
                        Log.i(TAG, "event: GuideExit reason=${event.reason}")
                    is NaviSdkBusinessEvent.RerouteResult ->
                        Log.i(
                            TAG,
                            "event: RerouteResult success=${event.success} errorCode=${event.errorCode}",
                        )
                    is NaviSdkBusinessEvent.RouteCalculated ->
                        Log.i(
                            TAG,
                            "event: RouteCalculated stage=${event.stage} pathCount=${event.pathCount}",
                        )
                    is NaviSdkBusinessEvent.RouteFailed ->
                        Log.w(
                            TAG,
                            "event: RouteFailed stage=${event.stage} errorCode=${event.errorCode}",
                        )
                    is NaviSdkBusinessEvent.StartNaviClicked ->
                        Log.i(
                            TAG,
                            "event: StartNaviClicked dest=${event.config.destinationName} pathIndex=${event.config.pathIndex}",
                        )
                    is NaviSdkBusinessEvent.PermissionsRequired ->
                        Log.i(
                            TAG,
                            "event: PermissionsRequired reason=${event.reason} perms=${event.permissions}",
                        )
                    is NaviSdkBusinessEvent.MapStyleFailed ->
                        Log.e(TAG, "event: MapStyleFailed errorCode=${event.message}")
                }
            }
        }
    }

    private companion object {
        const val TAG = "NaviSdkDemo"
    }
}
