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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
internal fun CustomScreen(
    viewModel: HjmViewModel,
    onBack: () -> Unit,
) {
    val customUiState by viewModel.customUiState.collectAsStateWithLifecycle(
        lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
    )
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            viewModel.onBackEvent.collectLatest {
                if (it) {
                    onBack()
                }
            }
        }

        scope.launch {
            viewModel.snackBarMessage.collectLatest {
                snackBarHostState.showSnackbar(it)
            }
        }
    }

    CustomScreen(
        snackBarHostState = snackBarHostState,
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
    snackBarHostState : SnackbarHostState,
    customUiState: CustomUiState,
    onActions: (CustomActions) -> Unit,
) {
    val scrollState = rememberScrollState()
    var selectedIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "HiJackMocker",
                        fontSize = 16.sp
                    ) },
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
                    ) {
                        Text(
                            text = "Send",
                            color = Color(0xFF007BF7),
                        )
                    }
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "close",
                        modifier = Modifier.clickable { onActions(CustomActions.Navigates.Back) },
                    )
                }
            )
        },
        containerColor = Color.White,
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        }
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