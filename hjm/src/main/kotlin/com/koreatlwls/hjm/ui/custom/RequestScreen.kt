package com.koreatlwls.hjm.ui.custom

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
import com.koreatlwls.hjm.model.ApiUiState
import com.koreatlwls.hjm.model.CustomActions
import com.koreatlwls.hjm.model.CustomUiState
import com.koreatlwls.hjm.ui.component.KeyValueRow

@Composable
internal fun RequestScreen(
    apiUiState: ApiUiState,
    requestUiState: CustomUiState.RequestUiState,
    onActions: (CustomActions) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        if (apiUiState.queryKeys.isNotEmpty()) {
            Text(
                text = "Query",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

        apiUiState.queryKeys.forEachIndexed { index, key ->
            KeyValueRow(
                key = key,
                value = apiUiState.queryValues[index],
                onValueChange = {
                    onActions(CustomActions.Updates.RequestQueryValue(index, it as String))
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

            Spacer(modifier = Modifier.height(8.dp))
        }

        requestUiState.headerKeys.forEachIndexed { index, key ->
            KeyValueRow(
                key = key,
                value = requestUiState.headerValues[index],
                onValueChange = {
                    onActions(CustomActions.Updates.RequestHeaderValue(index, it as String))
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
        }

        BodyItemList(
            items = requestUiState.bodyItems,
            onBodyValueChange = { key, value ->
                onActions(
                    CustomActions.Updates.RequestBodyValue(
                        bodyItems = requestUiState.bodyItems,
                        key = key,
                        newValue = value
                    )
                )
            },
        )
    }
}