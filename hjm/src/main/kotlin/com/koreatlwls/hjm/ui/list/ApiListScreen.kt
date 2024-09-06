package com.koreatlwls.hjm.ui.list

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.koreatlwls.hjm.data.HjmDataStore
import com.koreatlwls.hjm.model.ApiActions
import com.koreatlwls.hjm.model.ApiUiState
import com.koreatlwls.hjm.ui.HjmViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

@Composable
internal fun ApiListScreen(
    viewModel: HjmViewModel,
    onNavigateToCustom: () -> Unit,
) {
    val apiList = viewModel.apiUiStateList
    val clickedResponse by viewModel.clickedResponse

    LaunchedEffect(clickedResponse) {
        if (clickedResponse != null) {
            onNavigateToCustom()
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ApiListScreen(
    apiList: ImmutableList<ApiUiState>,
    onActions: (ApiActions) -> Unit,
) {
    val context = LocalContext.current
    val hjmDataStore = remember {
        HjmDataStore(context)
    }
    val scope = rememberCoroutineScope()

    BackHandler {
        onActions(ApiActions.Updates.DeleteAllApi)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                actions = {
                    TextButton(
                        onClick = {
                            scope.launch {
                                hjmDataStore.setHjmMode(false)
                                onActions(ApiActions.Updates.DeleteAllApi)
                            }
                        },
                    ) {
                        Text(
                            text = "SKIP",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color(0xFFF85752)
                        )
                    }
                }
            )
        },
        containerColor = Color.Transparent,
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            itemsIndexed(apiList) { index, item ->
                Column {
                    ApiItem(
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

            item {
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onActions(ApiActions.Updates.DeleteAllApi) },
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
}