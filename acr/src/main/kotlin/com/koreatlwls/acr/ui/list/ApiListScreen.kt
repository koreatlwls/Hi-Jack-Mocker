package com.koreatlwls.acr.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.koreatlwls.acr.model.ApiActions
import com.koreatlwls.acr.model.ApiUiState
import com.koreatlwls.acr.ui.AcrViewModel
import com.koreatlwls.acr.ui.component.ApiListItem
import com.koreatlwls.acr.util.composableActivityViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.collectLatest

@Composable
internal fun ApiListScreen(
    viewModel: AcrViewModel = composableActivityViewModel(),
    onNavigateToCustom: () -> Unit,
    onFinish: () -> Unit,
) {
    val apiList = viewModel.apiUiStateList
    val clickedResponse by viewModel.clickedResponse

    LaunchedEffect(clickedResponse) {
        if (clickedResponse != null) {
            onNavigateToCustom()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onFinishEvent.collectLatest {
            if (it) {
                onFinish()
            }
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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column {
            LazyColumn {
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

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onActions(ApiActions.Updates.DeleteAllApi) }
            ) {
                Text(
                    text = "SEND ALL",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }
        }
    }
}