package com.koreatlwls.acr.util

import com.koreatlwls.acr.data.AcrInterceptor

interface AcrManager {
    fun getInterceptor(): AcrInterceptor

    suspend fun setAcrMode(enable: Boolean)
}