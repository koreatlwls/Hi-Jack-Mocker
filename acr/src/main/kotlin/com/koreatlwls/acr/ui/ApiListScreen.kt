package com.koreatlwls.acr.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.koreatlwls.acr.model.ApiUiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun ApiListScreen(
    viewModel: AcrListViewModel = hiltViewModel()
) {
    val apiList = viewModel.apiUiStateList

    ApiListScreen(apiList = apiList.toImmutableList())
}

@Composable
private fun ApiListScreen(
    apiList: ImmutableList<ApiUiState>,
) {
    Scaffold(containerColor = Color.Transparent) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            contentAlignment = Alignment.Center
        ) {
            LazyColumn(modifier = Modifier.wrapContentSize()) {
                itemsIndexed(apiList) { index, item ->
                    Text(text = item.path)
                }
            }
        }
    }
}