package com.koreatlwls.acr.data

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import com.koreatlwls.acr.ui.AcrActivity
import com.koreatlwls.acr.util.InterceptorManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.CompletableFuture

class AcrInterceptor(
    private val context: Context,
    private val interceptorManager: InterceptorManager,
    private val acrDataStore: AcrDataStore,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        val future = CompletableFuture<Response>()

        CoroutineScope(Dispatchers.Default).launch {
            if (acrDataStore.getAcrMode()) {
                interceptorManager.sendWithInterceptorChannel(response)

                startAcrActivityIfNeeded(context)

                future.complete(interceptorManager.receiveWithResultChannel())
            } else {
                future.complete(response)
            }
        }

        return future.get()
    }

    private fun startAcrActivityIfNeeded(context: Context) {
        if (!interceptorManager.isAcrActivityRunning.get()) {
            interceptorManager.isAcrActivityRunning.set(true)
            val intent = Intent(context, AcrActivity::class.java)
                .apply { addFlags(FLAG_ACTIVITY_NEW_TASK) }
            context.startActivity(intent)
        }
    }

}
