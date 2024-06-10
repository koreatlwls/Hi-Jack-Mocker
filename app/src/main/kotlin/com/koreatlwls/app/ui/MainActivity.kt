package com.koreatlwls.app.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.koreatlwls.app.ui.theme.ApiCustomRequesterTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ApiCustomRequesterTheme {
                MainScreen()
            }
        }
    }
}

@Composable
internal fun MainScreen(viewModel: MainViewModel = hiltViewModel()){
    val pokemons by viewModel.pokemons.collectAsStateWithLifecycle(
        lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
    )

    Scaffold{
         LazyColumn(
             modifier = Modifier
                 .fillMaxSize()
                 .padding(it),
             verticalArrangement = Arrangement.spacedBy(8.dp)
         ){
             items(pokemons.size){
                 Text(
                     text = pokemons[it].name,
                     fontSize = 16.sp,
                 )
             }
         }
    }
}
