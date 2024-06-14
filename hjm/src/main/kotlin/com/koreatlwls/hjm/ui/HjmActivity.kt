package com.koreatlwls.hjm.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.koreatlwls.hjm.ui.navigation.hjmNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HjmActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                hjmNavHost(onFinish = { finish() })
            }
        }
    }
}

