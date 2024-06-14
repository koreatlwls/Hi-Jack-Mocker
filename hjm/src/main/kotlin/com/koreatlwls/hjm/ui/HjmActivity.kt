package com.koreatlwls.hjm.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import com.koreatlwls.hjm.ui.navigation.HjmNavHost

class HjmActivity : ComponentActivity() {

    private val hjmViewModel: HjmViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                HjmNavHost(
                    viewModel = hjmViewModel,
                    onFinish = { finish() })

            }
        }
    }
}

