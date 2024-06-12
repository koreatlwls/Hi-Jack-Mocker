package com.koreatlwls.acr.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.koreatlwls.acr.model.ApiActions
import com.koreatlwls.acr.model.ApiUiState

@Composable
internal fun ApiListItem(
    index: Int,
    apiUiState: ApiUiState,
    onActions: (ApiActions) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onActions(ApiActions.Updates.ClickApi(index)) }
    ) {
        Text(
            text = apiUiState.code.toString(),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = if (apiUiState.code in 200..299) Color.Green else Color.Red
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row {
                Text(
                    text = apiUiState.method,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = apiUiState.path,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = apiUiState.host,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }


        TextButton(
            onClick = { onActions(ApiActions.Updates.DeleteApi(index)) }
        ) {
            Text(
                text = "SEND",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Blue
            )
        }
    }
}

@Preview
@Composable
private fun ApiListItemPreview() {
    MaterialTheme {
        ApiListItem(
            index = 1,
            apiUiState = ApiUiState(
                method = "GET",
                scheme = "https",
                host = "naver.com",
                path = "/v1/abc/abc",
                code = 200
            ),
            onActions = {}
        )
    }
}