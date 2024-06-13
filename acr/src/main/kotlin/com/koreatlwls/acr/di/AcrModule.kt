package com.koreatlwls.acr.di

import android.content.Context
import com.koreatlwls.acr.data.AcrInterceptor
import com.koreatlwls.acr.util.AcrManager
import dagger.Module
import dagger.Provides
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
        acrManager: AcrManager,
    ): AcrInterceptor = AcrInterceptor(context, acrManager)

    @Provides
    @Singleton
    fun provideAcrManager(): AcrManager = AcrManager()
}