package com.koreatlwls.hjm.ui.custom

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.koreatlwls.hjm.model.CustomActions
import com.koreatlwls.hjm.model.CustomUiState
import com.koreatlwls.hjm.ui.HjmViewModel
import com.koreatlwls.hjm.ui.component.TabRow
import com.koreatlwls.hjm.util.composableActivityViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
internal fun CustomScreen(
    viewModel: HjmViewModel = composableActivityViewModel(),
    onBack: () -> Unit,
) {
    val customUiState by viewModel.customUiState.collectAsStateWithLifecycle(
        lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
    )

    LaunchedEffect(Unit) {
        viewModel.onBackEvent.collectLatest {
            if (it) {
                onBack()
            }
        }
    }

    CustomScreen(
        customUiState = customUiState,
        onActions = { actions ->
            when (actions) {
                is CustomActions.Navigates.Back -> {
                    viewModel.handleCustomActions(CustomActions.Updates.InitClickApi)
                    onBack()
                }

                is CustomActions.Updates -> viewModel.handleCustomActions(actions)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CustomScreen(
    customUiState: CustomUiState,
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
                    TextButton(
                        onClick = {
                            if (selectedIndex == 0) {
                                onActions(CustomActions.Updates.NewRequest)
                            } else {
                                onActions(CustomActions.Updates.NewResponse)
                            }
                        },
                        colors = ButtonDefaults.textButtonColors(contentColor = Color.Black),
                    ) {
                        Text(text = "Send")
                    }
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
                text = customUiState.apiUiState.path,
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
                        apiUiState = customUiState.apiUiState,
                        requestUiState = customUiState.requestUiState,
                        onActions = onActions,
                    )
                }

                else -> {
                    ResponseScreen(
                        responseUiState = customUiState.responseUiState,
                        onActions = onActions
                    )
                }
            }
        }
    }
}