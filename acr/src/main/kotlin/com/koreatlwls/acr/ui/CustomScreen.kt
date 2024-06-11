package com.koreatlwls.acr.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.koreatlwls.acr.model.CustomActions
import com.koreatlwls.acr.model.CustomUiState
import com.koreatlwls.acr.ui.component.TabRow

@Composable
internal fun CustomScreen(
    viewModel: CustomViewModel = hiltViewModel(),
    onFinish: () -> Unit,
) {
    val acrUiState by viewModel.customUiState
    val onFinishEvent by viewModel.onFinishEvent

    LaunchedEffect(onFinishEvent) {
        if (onFinishEvent) {
            onFinish()
        }
    }

    CustomScreen(
        arcUiState = acrUiState,
        onActions = { actions ->
            when (actions) {
                is CustomActions.Navigates.Back -> onFinish()
                is CustomActions.Updates -> viewModel.handleActions(actions)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CustomScreen(
    arcUiState: CustomUiState,
    onActions: (CustomActions) -> Unit,
) {
    val scrollState = rememberScrollState()
    var selectedIndex by remember {
        mutableIntStateOf(0)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "ApiCustomRequester") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
                actions = {
                    Icon(
                        modifier = Modifier.clickable {
                            if (selectedIndex == 0) {
                                onActions(CustomActions.Updates.NewRequest)
                            } else {
                                onActions(CustomActions.Updates.NewResponse)
                            }
                        },
                        imageVector = Icons.Filled.Done,
                        contentDescription = "done"
                    )
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "close",
                        modifier = Modifier.clickable { onActions(CustomActions.Navigates.Back) }
                    )
                }
            )
        },
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = arcUiState.path,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            TabRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                selectedItemIndex = selectedIndex,
                items = listOf("Request", "Response"),
                onClick = { index -> selectedIndex = index }
            )

            Spacer(modifier = Modifier.height(16.dp))

            when (selectedIndex) {
                0 -> {
                    RequestScreen(
                        requestUiState = arcUiState.requestUiState,
                        onActions = onActions,
                    )
                }

                else -> {
                    ResponseScreen(
                        responseUiState = arcUiState.responseUiState,
                        onActions = onActions
                    )
                }
            }
        }
    }
}