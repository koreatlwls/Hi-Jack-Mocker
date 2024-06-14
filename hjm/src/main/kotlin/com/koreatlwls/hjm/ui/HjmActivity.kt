package com.koreatlwls.hjm.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.koreatlwls.hjm.ui.navigation.HjmNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HjmActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                HjmNavHost(onFinish = { finish() })
            }
        }
    }
}

