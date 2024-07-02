package com.koreatlwls.hjm.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun TabRow(
    selectedItemIndex: Int,
    items: List<String>,
    modifier: Modifier = Modifier,
    onClick: (index: Int) -> Unit,
) {
    BoxWithConstraints(modifier = modifier) {
        val tabWidth = maxWidth / items.size
        val tabHeight = maxHeight

        val indicatorOffset: Dp by animateDpAsState(
            targetValue = tabWidth * selectedItemIndex,
            animationSpec = tween(easing = LinearEasing),
            label = "indicatorOffset"
        )

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFF6F7F9))
        ) {
            TabRowIndicator(
                indicatorWidth = tabWidth,
                indicatorHeight = tabHeight,
                indicatorOffset = indicatorOffset
            )

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                items.forEachIndexed { index, text ->
                    val isSelected = index == selectedItemIndex
                    TabRowItem(
                        isSelected = isSelected,
                        onClick = {
                            onClick(index)
                        },
                        tabWidth = tabWidth,
                        text = text
                    )
                }
            }
        }
    }
}

@Composable
private fun TabRowIndicator(
    indicatorWidth: Dp,
    indicatorHeight: Dp,
    indicatorOffset: Dp,
) {
    Box(
        modifier = Modifier
            .offset(
                x = indicatorOffset + 4.dp,
                y = 4.dp
            )
            .clip(RoundedCornerShape(8.dp))
            .height(indicatorHeight - 4.dp * 2)
            .width(indicatorWidth - 4.dp * 2)
            .background(Color.White)
    )
}

@Composable
private fun TabRowItem(
    isSelected: Boolean,
    onClick: () -> Unit,
    tabWidth: Dp,
    text: String,
) {
    val tabTextColor: Color by animateColorAsState(
        targetValue = if (isSelected) {
            Color(0xFF0E131B)
        } else {
            Color(0xFF7B8EA3)
        },
        label = "tabTextColor"
    )

    Box(
        modifier = Modifier
            .clickable {
                onClick()
            }
            .width(tabWidth)
            .fillMaxHeight(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = tabTextColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
        )
    }

}