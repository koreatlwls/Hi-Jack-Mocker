package com.koreatlwls.acr.ui.navigation

import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.koreatlwls.acr.ui.custom.CustomScreen
import com.koreatlwls.acr.ui.list.ApiListScreen

@Composable
internal fun AcrNavHost(onFinish: () -> Unit) {
    val navController = rememberNavController()

    NavHost(
        modifier = Modifier.wrapContentSize(),
        navController = navController,
        startDestination = "apiList"
    ) {
        composable("apiList") {
            ApiListScreen(
                onNavigateToCustom = {
                    navController.navigate("custom")
                },
                onFinish = onFinish,
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