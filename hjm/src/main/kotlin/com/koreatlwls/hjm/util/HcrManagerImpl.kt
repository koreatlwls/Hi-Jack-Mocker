package com.koreatlwls.hjm.util

import com.koreatlwls.hjm.data.HjmDataStore
import com.koreatlwls.hjm.data.HjmInterceptor
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class HjmManagerImpl @Inject constructor(
    private val hjmInterceptor: HjmInterceptor,
    private val hjmDataStore: HjmDataStore,
) : HjmManager {
    override fun getInterceptor(): HjmInterceptor {
        return hjmInterceptor
    }

    override suspend fun setHjmMode(enable: Boolean) {
        hjmDataStore.setHjmMode(enable)
    }

    override fun getHjmMode(): Flow<Boolean> {
        return hjmDataStore.getHjmModeFlow()
    }
}