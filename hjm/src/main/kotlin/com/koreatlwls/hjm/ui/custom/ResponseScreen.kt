package com.koreatlwls.hjm.ui.custom

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.koreatlwls.hjm.model.CustomActions
import com.koreatlwls.hjm.model.CustomUiState
import com.koreatlwls.hjm.model.JsonItem
import com.koreatlwls.hjm.ui.component.KeyValueRow
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun  ResponseScreen(
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

            Spacer(modifier = Modifier.height(4.dp))
        }

        BodyItemList(
            items = responseUiState.bodyItems,
            onBodyValueChange = { key, value ->
                onActions(
                    CustomActions.Updates.ResponseBodyValue(
                        bodyItems = responseUiState.bodyItems,
                        key = key,
                        newValue = value
                    )
                )
            },
        )
    }
}

@Composable
internal fun BodyItemList(
    modifier : Modifier = Modifier,
    items: ImmutableList<JsonItem>,
    onBodyValueChange: (key: String, value: Any) -> Unit,
) {
    Column(modifier) {
        items.forEach {
            BodyItem(
                item = it,
                onBodyValueChange = onBodyValueChange,
            )
        }
    }
}

@Composable
private fun BodyItem(
    item: JsonItem,
    onBodyValueChange: (key: String, value: Any) -> Unit,
) {
    when (item) {
        is JsonItem.SingleItem -> {
            KeyValueRow(
                key = item.key,
                value = item.value,
                onValueChange = {
                    onBodyValueChange(item.key, it)
                },
            )

            Spacer(modifier = Modifier.height(4.dp))
        }

        is JsonItem.ArrayGroup -> {
            ExpandableBodyItems(
                key = item.key,
                items = item.items,
                onBodyValueChange = onBodyValueChange
            )
        }

        is JsonItem.ObjectGroup -> {
            ExpandableBodyItems(
                key = item.key,
                items = item.items,
                onBodyValueChange = onBodyValueChange
            )
        }
    }
}

@Composable
private fun ExpandableBodyItems(
    key: String,
    items: ImmutableList<JsonItem>,
    onBodyValueChange: (key: String, value: Any) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val rotationAngle by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = ""
    )

    Column(modifier = Modifier.clickable { expanded = !expanded }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = key,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )

            Icon(
                modifier = Modifier.rotate(rotationAngle),
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null
            )
        }

        AnimatedVisibility(visible = expanded) {
            BodyItemList(
                modifier= Modifier.padding(start = 4.dp),
                items = items,
                onBodyValueChange = onBodyValueChange,
            )
        }
    }
}