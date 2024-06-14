package com.koreatlwls.hjm.ui.navigation

import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.koreatlwls.hjm.ui.HjmViewModel
import com.koreatlwls.hjm.ui.custom.CustomScreen
import com.koreatlwls.hjm.ui.list.ApiListScreen

@Composable
internal fun HjmNavHost(
    viewModel: HjmViewModel,
    onFinish: () -> Unit
) {
    val navController = rememberNavController()

    NavHost(
        modifier = Modifier.wrapContentSize(),
        navController = navController,
        startDestination = "apiList"
    ) {
        composable("apiList") {
            ApiListScreen(
                viewModel = viewModel,
                onNavigateToCustom = {
                    navController.navigate("custom")
                },
                onFinish = onFinish,
            )
        }

        composable(route = "custom") {
            CustomScreen(
                viewModel = viewModel,
                onBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}