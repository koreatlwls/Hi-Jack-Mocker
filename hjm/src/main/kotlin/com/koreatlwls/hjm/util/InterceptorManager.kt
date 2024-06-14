package com.koreatlwls.hjm.util

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.channels.consumeEach
import okhttp3.Response
import java.util.concurrent.atomic.AtomicBoolean

class InterceptorManager {
    private var interceptorChannel = Channel<Response>(UNLIMITED)
    private var resultChannel = Channel<Response>(UNLIMITED)
    val isHjmActivityRunning = AtomicBoolean(false)

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun sendWithInterceptorChannel(response: Response) {
        if (interceptorChannel.isClosedForSend) {
            interceptorChannel = Channel(UNLIMITED)
        }
        interceptorChannel.send(response)
    }

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun consumeEachInterceptorChannel(action: (Response) -> Unit) {
        if (interceptorChannel.isClosedForReceive) {
            interceptorChannel = Channel(UNLIMITED)
        }

        interceptorChannel.consumeEach {
            action(it)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun sendWithResultChannel(response: Response) {
        if (resultChannel.isClosedForSend) {
            resultChannel = Channel(UNLIMITED)
        }
        resultChannel.send(response)
    }

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun receiveWithResultChannel(): Response {
        if (resultChannel.isClosedForReceive) {
            resultChannel = Channel(UNLIMITED)
        }

        return resultChannel.receive()
    }

}