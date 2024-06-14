package com.koreatlwls.hjm

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.widget.FrameLayout
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import com.koreatlwls.hjm.data.HjmDataStore
import com.koreatlwls.hjm.data.HjmInterceptor
import com.koreatlwls.hjm.data.InterceptorManager
import com.koreatlwls.hjm.ui.HjmActivity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient

object HiJackMocker {
    internal lateinit var interceptorManager: InterceptorManager
    private lateinit var hjmDataStore: HjmDataStore
    private lateinit var hjmInterceptor: HjmInterceptor

    fun initialize(context: Context) {
        require(context is Application) {
            "Context must be an instance of Application"
        }

        hjmDataStore = HjmDataStore(context = context.applicationContext)
        interceptorManager = InterceptorManager()
        hjmInterceptor =
            HjmInterceptor(
                context = context.applicationContext,
                interceptorManager = interceptorManager,
                hjmDataStore = hjmDataStore
            )

        context.addLifecycleCallbacks()
    }

    fun OkHttpClient.Builder.addHiJackMocker(): OkHttpClient.Builder =
        this.addInterceptor(hjmInterceptor)

    private fun Application.addLifecycleCallbacks() {
        registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
            override fun onActivityStarted(activity: Activity) {}

            override fun onActivityResumed(activity: Activity) {
                if (activity !is HjmActivity) {
                    addToggleButton(activity)
                }
            }

            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {}
        }
        )
    }

    private fun addToggleButton(activity: Activity) {
        val composeView = ComposeView(activity).apply {
            setContent {
                ComposeToggleButton()
            }
        }

        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
            topMargin = 100
        }

        activity.addContentView(composeView, params)
    }

    @Composable
    private fun ComposeToggleButton() {
        val checked by hjmDataStore.getHjmModeFlow().collectAsState(initial = false)
        val scope = rememberCoroutineScope()

        Button(
            onClick = {
                scope.launch {
                    hjmDataStore.setHjmMode(!checked)
                }
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(if (checked) "ON" else "OFF")
        }
    }
}
