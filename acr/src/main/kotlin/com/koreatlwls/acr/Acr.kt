package com.koreatlwls.acr

import android.content.Context
import com.koreatlwls.acr.data.AcrInterceptor
import com.koreatlwls.acr.di.AcrManagerEntryPoint
import com.koreatlwls.acr.util.AcrManager
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.Flow

object Acr {
    private lateinit var acrManager: AcrManager

    fun initialize(context: Context) {
        acrManager = EntryPointAccessors
            .fromApplication(
                context,
                AcrManagerEntryPoint::class.java
            ).getAcrManager()
    }

    fun getInterceptor(): AcrInterceptor = acrManager.getInterceptor()

    suspend fun setAcrMode(enable: Boolean) {
        acrManager.setAcrMode(enable)
    }

    fun getAcrMode(): Flow<Boolean> = acrManager.getAcrMode()
}