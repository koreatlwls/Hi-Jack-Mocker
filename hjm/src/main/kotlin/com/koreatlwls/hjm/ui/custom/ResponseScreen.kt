package com.koreatlwls.hjm.ui.custom

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.koreatlwls.hjm.model.CustomActions
import com.koreatlwls.hjm.model.CustomUiState

@Composable
internal fun ResponseScreen(
    responseUiState: CustomUiState.ResponseUiState,
    onActions: (CustomActions) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        if (responseUiState.bodyItems.isNotEmpty()) {
            Text(
                text = "Body",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        BodyList(
            rootItem = null,
            rootKey = null,
            items = responseUiState.bodyItems,
            isRequestBody = false,
            onActions = onActions,
        )
    }
}