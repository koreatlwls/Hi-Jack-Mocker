package com.koreatlwls.acr.data

import android.content.Context
import android.content.Intent
import com.koreatlwls.acr.ui.AcrActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.CompletableFuture

class AcrInterceptor(
    private val context: Context,
    private val sendChannel: Channel<Response>,
    private val receiveChannel: Channel<Response>,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response  {
        val response = chain.proceed(chain.request())

        val future = CompletableFuture<Response>()

        CoroutineScope(Dispatchers.Default).launch {
            sendChannel.send(response)
        }

        CoroutineScope(Dispatchers.Default).launch {
            future.complete(receiveChannel.receive())
        }

        return future.get()
    }

}