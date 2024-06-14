package com.koreatlwls.hjm.data

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import com.koreatlwls.hjm.ui.HjmActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.CompletableFuture

internal class HjmInterceptor(
    context: Context,
    private val interceptorManager: InterceptorManager,
    private val hjmDataStore: HjmDataStore,
) : Interceptor {
    private val applicationContext = context.applicationContext

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        val future = CompletableFuture<Response>()

        CoroutineScope(Dispatchers.Default).launch {
            if (hjmDataStore.getHjmMode()) {
                interceptorManager.sendWithInterceptorChannel(response)

                startHjmActivityIfNeeded()

                future.complete(interceptorManager.receiveWithResultChannel())
            } else {
                future.complete(response)
            }
        }

        return future.get()
    }

    private fun startHjmActivityIfNeeded() {
        if (!interceptorManager.isHjmActivityRunning.get()) {
            interceptorManager.isHjmActivityRunning.set(true)
            val intent = Intent(applicationContext, HjmActivity::class.java)
                .apply { addFlags(FLAG_ACTIVITY_NEW_TASK) }
            applicationContext.startActivity(intent)
        }
    }

}
