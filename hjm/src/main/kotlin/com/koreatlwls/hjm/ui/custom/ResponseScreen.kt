package com.koreatlwls.hjm.ui.custom

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.koreatlwls.hjm.model.CustomActions
import com.koreatlwls.hjm.model.CustomUiState
import com.koreatlwls.hjm.model.JsonItem
import com.koreatlwls.hjm.ui.component.AddDeleteIconButton
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
            rootItem = null,
            rootKey = null,
            items = responseUiState.bodyItems,
            isRequestBody = false,
            onActions = onActions,
        )
    }
}

@Composable
internal fun BodyItemList(
    modifier: Modifier = Modifier,
    rootItem: JsonItem?,
    rootKey: String?,
    items: ImmutableList<JsonItem>,
    isRequestBody: Boolean,
    onActions: (CustomActions) -> Unit,
) {
    Column(modifier = modifier.padding(top = 8.dp)) {
        items.forEachIndexed { index, item ->
            BodyItem(
                rootItem = rootItem,
                rootKey = rootKey ?: "",
                item = item,
                index = if (rootKey == null) null else index,
                isRequestBody = isRequestBody,
                onActions = onActions,
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun BodyItem(
    rootItem: JsonItem?,
    rootKey: String,
    item: JsonItem,
    index: Int?,
    isRequestBody: Boolean,
    onActions: (CustomActions) -> Unit,
) {
    val updateRootKey = if (rootKey.isEmpty()) "" else "$rootKey."

    when (item) {
        is JsonItem.SingleItem -> {
            KeyValueRow(
                key = "$updateRootKey${item.key}",
                value = item.value,
                isCanDelete = item.isCanDelete,
                onValueChange = {
                    onActions(
                        CustomActions.Updates.UpdateBodyValue(
                            isRequestBody = isRequestBody,
                            id = item.id,
                            newValue = it
                        )
                    )
                },
            )
        }

        is JsonItem.ArrayGroup -> {
            ExpandableBodyItems(
                rootItem = rootItem,
                item = item,
                index = index,
                items = item.items,
                expanded = item.expanded,
                isCanAdd = item.isCanAdd,
                isCanDelete = item.isCanDelete,
                isRequestBody = isRequestBody,
                onActions = onActions,
            )
        }

        is JsonItem.ObjectGroup -> {
            ExpandableBodyItems(
                rootItem = rootItem,
                item = item,
                index = index,
                expanded = item.expanded,
                items = item.items,
                isCanDelete = item.isCanDelete,
                isRequestBody = isRequestBody,
                onActions = onActions
            )
        }
    }
}

@Composable
private fun ExpandableBodyItems(
    rootItem: JsonItem?,
    item: JsonItem,
    index: Int?,
    items: ImmutableList<JsonItem>,
    expanded: Boolean,
    isCanDelete: Boolean,
    isCanAdd: Boolean = false,
    isRequestBody: Boolean,
    onActions: (CustomActions) -> Unit,
) {
    val updateIndex = if (index == null) "" else "[${index}]"
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
                .clickable {
                    onActions(
                        CustomActions.Updates.UpdateBodyExpanded(
                            isRequestBody = isRequestBody,
                            id = item.id
                        )
                    )
                }
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
                text = "${item.key}$updateIndex",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )

            AddDeleteIconButton(
                isCanAdd = isCanAdd,
                isCanDelete = isCanDelete,
                onAddClick = {},
                onDeleteClick = {
                    if (rootItem != null && index != null) {
                        onActions(
                            CustomActions.Updates.DeleteBodyItem(
                                isRequestBody = isRequestBody,
                                id = rootItem.id,
                                index = index
                            )
                        )
                    }
                },
            )
        }
    }

    AnimatedVisibility(
        visible = expanded,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically(),
    ) {
        BodyItemList(
            modifier = Modifier.padding(start = 4.dp),
            rootItem = item,
            rootKey = "${item.key}$updateIndex",
            items = items,
            isRequestBody = isRequestBody,
            onActions = onActions,
        )
    }
}