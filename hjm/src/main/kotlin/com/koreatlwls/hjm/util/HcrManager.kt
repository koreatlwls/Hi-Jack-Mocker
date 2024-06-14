package com.koreatlwls.hjm.util

import com.koreatlwls.hjm.data.HjmInterceptor
import kotlinx.coroutines.flow.Flow

interface HjmManager {
    fun getInterceptor(): HjmInterceptor

    suspend fun setHjmMode(enable: Boolean)

    fun getHjmMode(): Flow<Boolean>
}