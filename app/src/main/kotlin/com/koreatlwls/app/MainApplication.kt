package com.koreatlwls.app

import android.app.Application
import com.koreatlwls.acr.AcrFactory
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        AcrFactory.initialize(this)
    }
}