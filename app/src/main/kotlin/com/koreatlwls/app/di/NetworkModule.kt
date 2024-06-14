package com.koreatlwls.app.di

import com.koreatlwls.hjm.HiJackMocker.addHiJackMocker
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {
    private const val TEN_SECONDS = 10L

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .readTimeout(TEN_SECONDS, TimeUnit.SECONDS)
            .connectTimeout(TEN_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TEN_SECONDS, TimeUnit.SECONDS)
            .addHiJackMocker()
            .addInterceptor(getLoggingInterceptor())
            .build()

    private fun getLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi =
        Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()


    @Provides
    @Singleton
    fun provideRetrofit(
        moshi: Moshi,
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://pokeapi.co")
            .client(provideHttpClient())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
}