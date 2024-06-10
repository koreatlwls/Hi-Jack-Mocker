package com.koreatlwls.acr.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.koreatlwls.acr.model.AcrActions
import com.koreatlwls.acr.model.AcrUiState
import com.koreatlwls.acr.ui.component.KeyValueRow

@Composable
internal fun RequestScreen(
    requestUiState: AcrUiState.RequestUiState,
    onActions: (AcrActions) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        if (requestUiState.queryKeys.isNotEmpty()) {
            Text(
                text = "Query",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(4.dp))
        }

        requestUiState.queryKeys.forEachIndexed { index, key ->
            KeyValueRow(
                key = key,
                value = requestUiState.queryValues[index],
                onValueChange = {
                    onActions(AcrActions.Updates.RequestQueryValue(index, it))
                }
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

        if (requestUiState.headerKeys.isNotEmpty()) {
            Text(
                text = "Header",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(4.dp))
        }

        requestUiState.headerKeys.forEachIndexed { index, key ->
            KeyValueRow(
                key = key,
                value = requestUiState.headerValues[index],
                onValueChange = {
                    onActions(AcrActions.Updates.RequestHeaderValue(index, it))
                }
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

        if (requestUiState.bodyItems.isNotEmpty()) {
            Text(
                text = "Body",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(4.dp))
        }

        BodyItemList(
            items = requestUiState.bodyItems,
            onBodyValueChange = { key, value ->
                onActions(
                    AcrActions.Updates.RequestBodyValue(
                        bodyItems = requestUiState.bodyItems,
                        key = key,
                        newValue = value
                    )
                )
            },
        )
    }
}