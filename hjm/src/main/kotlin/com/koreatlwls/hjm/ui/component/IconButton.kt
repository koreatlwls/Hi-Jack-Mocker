package com.koreatlwls.hjm.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun AddDeleteIconButton(
    isCanDelete: Boolean,
    isCanAdd: Boolean = false,
    onDeleteClick: () -> Unit ,
    onAddClick: () -> Unit = {},
) {
    when {
        isCanAdd -> {
            AddDeleteIconButton(
                icon = Icons.Default.Add,
                backgroundColor = Color(0xFF007BF7),
                onClick = onAddClick,
            )
        }

        isCanDelete -> {
            AddDeleteIconButton(
                icon = Icons.Default.Clear,
                backgroundColor = Color(0xFFF85752),
                onClick = onDeleteClick,
            )
        }
    }
}

@Composable
private fun AddDeleteIconButton(
    icon: ImageVector,
    backgroundColor: Color,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .padding(start = 8.dp)
            .size(30.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(3.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
        )
    }
}