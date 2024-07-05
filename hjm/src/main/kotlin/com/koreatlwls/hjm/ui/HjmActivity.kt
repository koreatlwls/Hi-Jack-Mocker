package com.koreatlwls.hjm.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import com.koreatlwls.hjm.HiJackMocker.interceptorManager
import com.koreatlwls.hjm.ui.navigation.HjmNavHost
import kotlinx.coroutines.flow.collectLatest

class HjmActivity : ComponentActivity() {

    private val hjmViewModel: HjmViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                HjmNavHost(
                    viewModel = hjmViewModel,
                )
            }

            LaunchedEffect(Unit) {
                hjmViewModel.onFinishEvent.collectLatest {
                    interceptorManager.isHjmActivityRunning.set(false)
                    finish()
                }
            }
        }
    }
}

