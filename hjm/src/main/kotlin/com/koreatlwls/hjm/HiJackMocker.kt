package com.koreatlwls.hjm

import android.content.Context
import com.koreatlwls.hjm.data.HjmDataStore
import com.koreatlwls.hjm.data.HjmInterceptor
import com.koreatlwls.hjm.data.InterceptorManager
import kotlinx.coroutines.flow.Flow
import okhttp3.OkHttpClient

object HiJackMocker {
    internal lateinit var interceptorManager: InterceptorManager
    private lateinit var hjmDataStore: HjmDataStore
    private lateinit var hjmInterceptor: HjmInterceptor

    fun initialize(context: Context) {
        hjmDataStore = HjmDataStore(context = context.applicationContext)
        interceptorManager = InterceptorManager()
        hjmInterceptor =
            HjmInterceptor(
                context = context.applicationContext,
                interceptorManager = interceptorManager,
                hjmDataStore = hjmDataStore
            )
    }

    fun OkHttpClient.Builder.addHiJackMocker(): OkHttpClient.Builder =
        this.addInterceptor(hjmInterceptor)

    suspend fun setHjmMode(enable: Boolean) {
        hjmDataStore.setHjmMode(enable)
    }

    fun getHjmMode(): Flow<Boolean> = hjmDataStore.getHjmModeFlow()
}