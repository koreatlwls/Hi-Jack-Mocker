package com.koreatlwls.hjm.ui.custom

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.koreatlwls.hjm.extensions.isParentKey
import com.koreatlwls.hjm.model.CustomActions
import com.koreatlwls.hjm.model.CustomUiState
import com.koreatlwls.hjm.model.JsonItem
import com.koreatlwls.hjm.ui.component.KeyValueRow
import kotlinx.collections.immutable.ImmutableList

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
    modifier: Modifier = Modifier,
    items: ImmutableList<JsonItem>,
    onBodyValueChange: (key: String, value: Any) -> Unit,
) {
    Column(modifier = modifier.padding(top = 8.dp)) {
        items.forEach {
            BodyItem(
                item = it,
                onBodyValueChange = onBodyValueChange,
            )

            Spacer(modifier = Modifier.height(8.dp))
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

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(8.dp))
                .clickable { expanded = !expanded }
                .background(
                    color = Color(0xFFF6F7F9),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .rotate(rotationAngle),
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null
            )

            Text(
                modifier = Modifier.weight(1f),
                text = key,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )

            if (items.isNotEmpty()) {
                val (backgroundColor, icon, clickEvent) = if (key.isParentKey()) {
                    Triple(
                        Color(0xFF007BF7),
                        Icons.Default.Add,
                        {},
                    )
                } else {
                    Triple(
                        Color(0xFFF85752),
                        Icons.Default.Clear,
                        {},
                    )
                }
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            color = backgroundColor,
                            shape = RoundedCornerShape(6.dp),
                        )
                        .clickable { clickEvent() }
                        .padding(3.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        }
    }

    AnimatedVisibility(
        visible = expanded,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically(),
    ) {
        BodyItemList(
            modifier = Modifier.padding(start = 4.dp),
            items = items,
            onBodyValueChange = onBodyValueChange,
        )
    }
}