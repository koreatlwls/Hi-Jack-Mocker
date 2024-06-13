package com.koreatlwls.app

import android.app.Application
import com.koreatlwls.acr.Acr
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        Acr.initialize(this)
    }
}