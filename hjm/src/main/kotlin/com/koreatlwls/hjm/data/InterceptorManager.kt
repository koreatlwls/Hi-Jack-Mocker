package com.koreatlwls.hjm.data

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import okhttp3.Response
import java.util.concurrent.atomic.AtomicBoolean

class InterceptorManager {
    val isHjmActivityRunning = AtomicBoolean(false)

    private val _interceptorEvent = MutableSharedFlow<Pair<String, Response>>(replay = 1)
    val interceptorEvent = _interceptorEvent.asSharedFlow()

    private val _resultEvent = MutableSharedFlow<Pair<String, Response>>()
    val resultEvent = _resultEvent.asSharedFlow()

    suspend fun sendEventAtInterceptorEvent(uuid: String, response: Response) {
        _interceptorEvent.emit(Pair(uuid, response))
    }

    suspend fun sendEventAtResultEvent(uuid: String, response: Response) {
        _resultEvent.emit(Pair(uuid, response))
    }
}