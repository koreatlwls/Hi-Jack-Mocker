package com.koreatlwls.hjm.data

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import com.koreatlwls.hjm.ui.HjmActivity
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

internal class HjmInterceptor(
    context: Context,
    private val interceptorManager: InterceptorManager,
    private val hjmDataStore: HjmDataStore,
) : Interceptor {
    private val applicationContext = context.applicationContext

    override fun intercept(chain: Interceptor.Chain): Response = runBlocking {
        var response = chain.proceed(chain.request())

        if (hjmDataStore.getHjmMode()) {
            interceptorManager.sendWithInterceptorChannel(response)

            startHjmActivityIfNeeded()
            response = interceptorManager.receiveWithResultChannel()
        }

        return@runBlocking response
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
