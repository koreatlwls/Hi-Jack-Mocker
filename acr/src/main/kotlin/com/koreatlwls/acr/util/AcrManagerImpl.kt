package com.koreatlwls.acr.util

import com.koreatlwls.acr.data.AcrDataStore
import com.koreatlwls.acr.data.AcrInterceptor
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class AcrManagerImpl @Inject constructor(
    private val acrInterceptor: AcrInterceptor,
    private val acrDataStore: AcrDataStore,
) : AcrManager {
    override fun getInterceptor(): AcrInterceptor {
        return acrInterceptor
    }

    override suspend fun setAcrMode(enable: Boolean) {
        acrDataStore.setAcrMode(enable)
    }

    override fun getAcrMode(): Flow<Boolean> {
        return acrDataStore.getAcrModeFlow()
    }
}