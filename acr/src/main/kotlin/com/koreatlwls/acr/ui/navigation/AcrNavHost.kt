package com.koreatlwls.acr.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.koreatlwls.acr.ui.list.ApiListScreen
import com.koreatlwls.acr.ui.custom.CustomScreen

@Composable
internal fun AcrNavHost() {
    val navController = rememberNavController()

    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = "apiList"
    ) {
        composable("apiList") {
            ApiListScreen(
                onNavigateToCustom = {
                    navController.navigate("custom")
                }
            )
        }

        composable(route = "custom") {
            CustomScreen(
                onBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}