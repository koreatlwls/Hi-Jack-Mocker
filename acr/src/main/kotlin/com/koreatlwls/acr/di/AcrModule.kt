package com.koreatlwls.acr.di

import android.content.Context
import com.koreatlwls.acr.data.AcrDataStore
import com.koreatlwls.acr.data.AcrInterceptor
import com.koreatlwls.acr.util.AcrManager
import com.koreatlwls.acr.util.AcrManagerImpl
import com.koreatlwls.acr.util.InterceptorManager
import dagger.Module
import dagger.Provides
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object AcrModule {
    @Provides
    @Singleton
    fun provideAcrInterceptor(
        @ApplicationContext context: Context,
        interceptorManager: InterceptorManager,
        acrDataStore: AcrDataStore,
    ): AcrInterceptor = AcrInterceptor(
        context = context,
        interceptorManager = interceptorManager,
        acrDataStore = acrDataStore,
    )

    @Provides
    @Singleton
    fun provideAcrDataStore(@ApplicationContext context: Context): AcrDataStore =
        AcrDataStore(context)

    @Provides
    @Singleton
    fun provideInterceptorManager(): InterceptorManager = InterceptorManager()

    @Provides
    @Singleton
    fun provideAcrManager(
        acrInterceptor: AcrInterceptor,
        acrDataStore: AcrDataStore,
    ): AcrManager = AcrManagerImpl(acrInterceptor, acrDataStore)
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface AcrManagerEntryPoint {
    fun getAcrManager(): AcrManager
}