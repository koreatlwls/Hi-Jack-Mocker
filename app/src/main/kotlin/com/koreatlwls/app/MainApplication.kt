package com.koreatlwls.app

import android.app.Application
import com.koreatlwls.hjm.HiJackMocker
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        HiJackMocker.initialize(this)
    }
}