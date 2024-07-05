package com.koreatlwls.hjm.data

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Response
import java.util.concurrent.atomic.AtomicBoolean

class InterceptorManager {
    private val mutex = Mutex()
    private var interceptorChannel = Channel<Response>(UNLIMITED)
    private var resultChannel = Channel<Response>(UNLIMITED)
    val isHjmActivityRunning = AtomicBoolean(false)

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun sendWithInterceptorChannel(response: Response) {
        mutex.withLock {
            if (interceptorChannel.isClosedForSend) {
                interceptorChannel = Channel(UNLIMITED)
            }

            interceptorChannel.send(response)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun receiveAllWithInterceptorChannel(action: (Response) -> Unit) {
        mutex.withLock {
            if (interceptorChannel.isClosedForReceive) {
                interceptorChannel = Channel(UNLIMITED)
            }

            for (response in interceptorChannel) {
                action(response)
            }
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