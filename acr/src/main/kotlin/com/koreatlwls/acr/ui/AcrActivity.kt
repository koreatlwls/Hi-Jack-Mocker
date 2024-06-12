package com.koreatlwls.acr.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.koreatlwls.acr.ui.navigation.AcrNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AcrActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                AcrNavHost()
            }
        }
    }
}

