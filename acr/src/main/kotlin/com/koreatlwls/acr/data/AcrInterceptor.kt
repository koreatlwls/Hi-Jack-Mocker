package com.koreatlwls.acr.data

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import com.koreatlwls.acr.ui.AcrActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.CompletableFuture

class AcrInterceptor(
    private val context: Context,
    private val sendChannel: Channel<Response>,
    private val receiveChannel: Channel<Response>,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        val future = CompletableFuture<Response>()

        CoroutineScope(Dispatchers.Default).launch {
            sendChannel.send(response)
        }

        CoroutineScope(Dispatchers.Default).launch {
            future.complete(receiveChannel.receive())
        }

        startAcrActivityIfNeeded(context)

        return future.get()
    }

    private fun startAcrActivityIfNeeded(context: Context) {
        if (!isAcrActivityRunning(context)) {
            val intent = Intent(context, AcrActivity::class.java)
                .apply { addFlags(FLAG_ACTIVITY_NEW_TASK) }
            context.startActivity(intent)
        }
    }

    private fun isAcrActivityRunning(context: Context): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningTasks = activityManager.getRunningTasks(1)
        if (runningTasks.isNotEmpty()) {
            val topActivity = runningTasks[0].topActivity
            return topActivity?.className == AcrActivity::class.java.name
        }
        return false
    }

}
