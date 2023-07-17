package com.beran.todo.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.beran.todo.R
import com.beran.todo.data.models.Priority
import com.beran.todo.ui.theme.PRIORITY_DROP_DOWN_HEIGHT
import com.beran.todo.ui.theme.PRIORITY_INDICATOR_SIZE
import com.beran.todo.ui.theme.TodoTheme

@Composable
fun PriorityDropDown(
    priority: Priority,
    onSelectedPriority: (Priority) -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    val angle: Float by animateFloatAsState(targetValue = if (expanded) 180f else 0f)
    var parentSize by remember {
        mutableStateOf(IntSize.Zero)
    }

    Row(modifier = Modifier
        .fillMaxWidth()
        .onGloballyPositioned {
            parentSize = it.size
        }
        .height(PRIORITY_DROP_DOWN_HEIGHT)
        .clickable {
            expanded = true
        }
        .border(
            width = 1.dp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            shape = MaterialTheme.shapes.small
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Canvas(modifier = Modifier
            .size(PRIORITY_INDICATOR_SIZE)
            .weight(1f), onDraw = {
            drawCircle(color = priority.color)
        })
        Text(
            text = priority.name,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(6f)
        )
        IconButton(
            modifier = Modifier
                .alpha(DefaultAlpha)
                .rotate(angle)
                .weight(1f),
            onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = stringResource(R.string.icon_dropdown_txt)
            )
        }
    }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        modifier = Modifier.width(with(LocalDensity.current) { parentSize.width.toDp() })
    ) {
        // ** slice used to select only the specify item
        Priority.values().slice(0..2).forEach { item ->
            DropdownMenuItem(
                text = {
                    PriorityItem(priority = item)
                }, onClick = {
                    expanded = false
                    onSelectedPriority(item)
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PriorityDropDownPrev() {
    TodoTheme {
        PriorityDropDown(priority = Priority.Medium, onSelectedPriority = {})
    }
}