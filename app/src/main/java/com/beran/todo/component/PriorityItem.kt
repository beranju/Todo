package com.beran.todo.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.beran.todo.data.models.Priority
import com.beran.todo.ui.theme.LARGE_PADDING
import com.beran.todo.ui.theme.PRIORITY_INDICATOR_SIZE
import com.beran.todo.ui.theme.TodoTheme

@Composable
fun PriorityItem(priority: Priority) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Canvas(modifier = Modifier.size(PRIORITY_INDICATOR_SIZE)) {
            drawCircle(color = priority.color)
        }
        Text(
            text = priority.name,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(start = LARGE_PADDING)
        )
    }
}

@Preview
@Composable
private fun PriorityItemPrev() {
    TodoTheme {
        PriorityItem(priority = Priority.Low)
    }
}