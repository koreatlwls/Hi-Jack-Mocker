package com.koreatlwls.acr.di

import android.content.Context
import com.koreatlwls.acr.data.AcrInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.channels.Channel
import okhttp3.Response
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object AcrModule {
    @Provides
    @Singleton
    fun provideAcrInterceptor(
        @ApplicationContext context: Context,
        @Named("send") sendChannel: Channel<Response>,
        @Named("receive") receiveChannel: Channel<Response>,
    ): AcrInterceptor = AcrInterceptor(context, sendChannel, receiveChannel)

    @Provides
    @Singleton
    @Named(value = "send")
    fun provideSendChannel() = Channel<Response>(Channel.CONFLATED)

    @Provides
    @Singleton
    @Named(value = "receive")
    fun provideReceiveChannel() = Channel<Response>(Channel.CONFLATED)
}