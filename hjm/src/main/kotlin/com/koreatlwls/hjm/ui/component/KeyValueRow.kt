package com.koreatlwls.hjm.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun KeyValueRow(
    key: String,
    value: Any,
    onValueChange: (Any) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(
                    color = Color(0xFFF6F7F9),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = key,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        when (value) {
            is Boolean -> {
                BooleanValue(
                    value = value,
                    onValueChange = onValueChange,
                )
            }

            is Int, is Long, is Float, is Double -> {
                NumberValue(
                    value = value,
                    onValueChange = onValueChange,
                )
            }

            else -> {
                TextValue(
                    value = value.toString(),
                    onValueChange = onValueChange
                )
            }
        }
    }
}

@Composable
private fun RowScope.BooleanValue(
    value: Boolean,
    onValueChange: (Any) -> Unit,
) {
    Row(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = value,
            onClick = { onValueChange(true as Any) }
        )

        Text(
            "True",
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
        )

        RadioButton(
            selected = !value,
            onClick = { onValueChange(false as Any) }
        )

        Text(
            "False",
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
        )
    }
}

@Composable
private fun RowScope.NumberValue(
    value: Any,
    onValueChange: (Any) -> Unit,
) {
    var textValue by remember { mutableStateOf(value.toString()) }
    Box(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        BasicTextField(
            modifier = Modifier.fillMaxWidth(),
            value = textValue,
            onValueChange = { newValue ->
                textValue = newValue
                val castedValue = when (value) {
                    is Int -> newValue.toIntOrNull()
                    is Long -> newValue.toLongOrNull()
                    is Float -> newValue.toFloatOrNull()
                    is Double -> newValue.toDoubleOrNull()
                    else -> null
                }
                castedValue?.let { onValueChange(it) }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
        )
    }
}

@Composable
private fun RowScope.TextValue(
    value: String,
    onValueChange: (Any) -> Unit,
) {
    Box(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        BasicTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
        )
    }
}

@Preview
@Composable
private fun KeyValueRowPreview() {
    MaterialTheme {
        KeyValueRow(
            key = "safsdafsdafsadfsdafsadfsdafsaddfsadfsdaf",
            value = false,
            onValueChange = {}
        )
    }
}