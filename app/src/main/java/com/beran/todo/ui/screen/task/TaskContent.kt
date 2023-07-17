package com.beran.todo.ui.screen.task

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.beran.todo.R
import com.beran.todo.component.PriorityDropDown
import com.beran.todo.data.models.Priority
import com.beran.todo.ui.theme.LARGE_PADDING
import com.beran.todo.ui.theme.MEDIUM_PADDING
import com.beran.todo.ui.theme.TodoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskContent(
    title: String,
    onTitleChange: (String) -> Unit,
    priority: Priority,
    onPriorityChange: (Priority) -> Unit,
    description: String,
    onChangeDescription: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(LARGE_PADDING)
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = onTitleChange,
            label = {
                Text(
                    text = stringResource(R.string.title_txt),
                    style = MaterialTheme.typography.labelMedium
                )
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(MEDIUM_PADDING))
        PriorityDropDown(
            priority = priority,
            onSelectedPriority = onPriorityChange
        )
        OutlinedTextField(
            value = description,
            onValueChange = onChangeDescription,
            label = {
                Text(
                    text = stringResource(R.string.description_txt),
                    style = MaterialTheme.typography.labelMedium
                )
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TaskContentPrev() {
    TodoTheme {
        TaskContent(
            title = "",
            onTitleChange = {},
            priority = Priority.Medium,
            onPriorityChange = {},
            description = "",
            onChangeDescription = {}
        )
    }
}