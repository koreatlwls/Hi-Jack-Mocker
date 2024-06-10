package com.koreatlwls.acr.data

import android.content.Context
import android.content.Intent
import com.koreatlwls.acr.ui.AcrActivity
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AcrInterceptor(
    private val context: Context,
    private val sendChannel: Channel<Response>,
    private val receiveChannel: Channel<Response>,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response = runBlocking {
        val response = chain.proceed(chain.request())

        sendChannel.send(response)

        val deferred = async {
            receiveChannel.receive()
        }

        val intent = Intent(context, AcrActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)

        return@runBlocking deferred.await()
    }

}