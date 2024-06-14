package com.koreatlwls.hjm

import android.content.Context
import com.koreatlwls.hjm.data.HjmInterceptor
import com.koreatlwls.hjm.di.HjmManagerEntryPoint
import com.koreatlwls.hjm.util.HjmManager
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.Flow
import okhttp3.OkHttpClient

object HiJackMocker {
    private lateinit var hjmManager: HjmManager

    fun initialize(context: Context) {
        hjmManager = EntryPointAccessors
            .fromApplication(
                context,
                HjmManagerEntryPoint::class.java
            ).getHjmManager()
    }

    fun OkHttpClient.Builder.addHiJackMocker() : OkHttpClient.Builder =
        this.addInterceptor(hjmManager.getInterceptor())

    suspend fun setHjmMode(enable: Boolean) {
        hjmManager.setHjmMode(enable)
    }

    fun getHjmMode(): Flow<Boolean> = hjmManager.getHjmMode()
}