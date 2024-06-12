package com.koreatlwls.acr.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.koreatlwls.acr.model.ApiActions
import com.koreatlwls.acr.model.ApiUiState
import com.koreatlwls.acr.ui.AcrViewModel
import com.koreatlwls.acr.util.composableActivityViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun ApiListScreen(
    viewModel: AcrViewModel = composableActivityViewModel(),
    onNavigateToCustom : () -> Unit,
) {
    val apiList = viewModel.apiUiStateList
    val clickedResponse by viewModel.clickedResponse

    LaunchedEffect(clickedResponse) {
        if(clickedResponse != null){
            onNavigateToCustom()
        }
    }

    ApiListScreen(
        apiList = apiList.toImmutableList(),
        onActions = { action ->
            when(action){
                is ApiActions.Updates -> viewModel.handleApiActions(action)
            }
        }
    )
}

@Composable
private fun ApiListScreen(
    apiList: ImmutableList<ApiUiState>,
    onActions : (ApiActions) -> Unit,
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
                    Text(
                        modifier = Modifier.clickable { onActions(ApiActions.Updates.ClickApi(index)) },
                        text = item.path
                    )
                }
            }
        }
    }
}