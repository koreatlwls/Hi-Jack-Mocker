package com.koreatlwls.acr.ui.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.koreatlwls.acr.model.ApiActions
import com.koreatlwls.acr.model.ApiUiState
import com.koreatlwls.acr.ui.AcrViewModel
import com.koreatlwls.acr.ui.component.ApiListItem
import com.koreatlwls.acr.util.composableActivityViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun ApiListScreen(
    viewModel: AcrViewModel = composableActivityViewModel(),
    onNavigateToCustom: () -> Unit,
    onFinish: () -> Unit,
) {
    val apiList = viewModel.apiUiStateList
    val clickedResponse by viewModel.clickedResponse
    val onFinishEvent by remember {
        derivedStateOf {
            apiList.size == 0
        }
    }

    LaunchedEffect(clickedResponse) {
        if (clickedResponse != null) {
            onNavigateToCustom()
        }
    }

    LaunchedEffect(onFinishEvent){
        if(onFinishEvent){
            onFinish()
        }
    }

    ApiListScreen(
        apiList = apiList.toImmutableList(),
        onActions = { action ->
            when (action) {
                is ApiActions.Updates -> viewModel.handleApiActions(action)
            }
        }
    )
}

@Composable
private fun ApiListScreen(
    apiList: ImmutableList<ApiUiState>,
    onActions: (ApiActions) -> Unit,
) {
    Scaffold(containerColor = Color.Transparent) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            contentAlignment = Alignment.Center
        ) {
            LazyColumn(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(horizontal = 4.dp)
            ) {
                itemsIndexed(apiList) { index, item ->
                    Column {
                        ApiListItem(
                            index = index,
                            apiUiState = item,
                            onActions = onActions,
                        )

                        if (index != apiList.lastIndex) {
                            Spacer(modifier = Modifier.height(8.dp))
                            HorizontalDivider(thickness = 1.dp, color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}