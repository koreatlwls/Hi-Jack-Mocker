package com.koreatlwls.hjm

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.koreatlwls.hjm.data.HjmDataStore
import com.koreatlwls.hjm.data.HjmInterceptor
import com.koreatlwls.hjm.data.InterceptorManager
import com.koreatlwls.hjm.ui.HjmActivity
import com.koreatlwls.hjm.ui.component.IconButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient

object HiJackMocker {
    internal lateinit var interceptorManager: InterceptorManager
    private lateinit var hjmDataStore: HjmDataStore
    private lateinit var hjmInterceptor: HjmInterceptor
    private var iconVisible by mutableStateOf(true)

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
        this.addInterceptor(
            Interceptor { chain ->
                if (this@HiJackMocker::hjmInterceptor.isInitialized) {
                    hjmInterceptor.intercept(chain)
                } else {
                    chain.proceed(chain.request())
                }
            }
        )

    private fun Application.addLifecycleCallbacks() {
        registerActivityLifecycleCallbacks(ActivityLifecycleCallbacksImpl())
    }

    private class ActivityLifecycleCallbacksImpl : Application.ActivityLifecycleCallbacks {
        private var composeView: ComposeView? = null

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) = Unit

        override fun onActivityStarted(activity: Activity) = Unit

        override fun onActivityResumed(activity: Activity) {
            if (activity !is HjmActivity) {
                addHjmModeButton(activity)
            }
        }

        override fun onActivityPaused(activity: Activity) = Unit

        override fun onActivityStopped(activity: Activity) = Unit

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit

        override fun onActivityDestroyed(activity: Activity) {
            if (activity !is HjmActivity) {
                removeHjmModeButton()
            }
        }

        private fun addHjmModeButton(activity: Activity) {
            composeView = ComposeView(activity).apply {
                setContent {
                    HjmModeButton()
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

        private fun removeHjmModeButton() {
            composeView?.let {
                (it as? ViewGroup)?.removeView(it)
            }
            composeView = null
        }

        @Composable
        private fun HjmModeButton() {
            val checked by hjmDataStore.getHjmModeFlow().collectAsState(initial = false)
            val scope = rememberCoroutineScope()

            if(iconVisible){
                IconButton(
                    onClick = {
                        scope.launch {
                            withContext(Dispatchers.IO) {
                                hjmDataStore.setHjmMode(!checked)
                            }
                        }
                    },
                    onLongClick = {
                        iconVisible = false
                    }
                ) {
                    Image(
                        modifier = Modifier.size(36.dp),
                        painter = painterResource(
                            id = if (checked) R.drawable.hjm_mode_on
                            else R.drawable.hjm_mode_off
                        ),
                        contentDescription = null,
                    )
                }
            }
        }
    }
}
