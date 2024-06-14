package com.koreatlwls.hjm.di

import android.content.Context
import com.koreatlwls.hjm.data.HjmDataStore
import com.koreatlwls.hjm.data.HjmInterceptor
import com.koreatlwls.hjm.util.HjmManager
import com.koreatlwls.hjm.util.HjmManagerImpl
import com.koreatlwls.hjm.util.InterceptorManager
import dagger.Module
import dagger.Provides
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object HjmModule {
    @Provides
    @Singleton
    fun provideHjmInterceptor(
        @ApplicationContext context: Context,
        interceptorManager: InterceptorManager,
        hjmDataStore: HjmDataStore,
    ): HjmInterceptor = HjmInterceptor(
        context = context,
        interceptorManager = interceptorManager,
        hjmDataStore = hjmDataStore,
    )

    @Provides
    @Singleton
    fun provideHjmDataStore(@ApplicationContext context: Context): HjmDataStore =
        HjmDataStore(context)

    @Provides
    @Singleton
    fun provideInterceptorManager(): InterceptorManager = InterceptorManager()

    @Provides
    @Singleton
    fun provideHjmManager(
        hjmInterceptor: HjmInterceptor,
        hjmDataStore: HjmDataStore,
    ): HjmManager = HjmManagerImpl(hjmInterceptor, hjmDataStore)
}

@EntryPoint
@InstallIn(SingletonComponent::class)
internal interface HjmManagerEntryPoint {
    fun getHjmManager(): HjmManager
}