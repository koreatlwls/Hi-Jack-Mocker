package com.koreatlwls.acr.util

import com.koreatlwls.acr.data.AcrInterceptor
import kotlinx.coroutines.flow.Flow

interface AcrManager {
    fun getInterceptor(): AcrInterceptor

    suspend fun setAcrMode(enable: Boolean)

    fun getAcrMode(): Flow<Boolean>
}