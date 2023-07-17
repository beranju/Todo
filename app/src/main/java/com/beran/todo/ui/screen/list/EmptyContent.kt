package com.beran.todo.ui.screen.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SentimentDissatisfied
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.beran.todo.R
import com.beran.todo.ui.theme.TodoTheme

@Composable
fun EmptyContent() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Icon(
            imageVector = Icons.Filled.SentimentDissatisfied,
            contentDescription = stringResource(R.string.sad_icon_hint),
            modifier = Modifier.size(120.dp)
        )
        Text(
            text = stringResource(R.string.no_tasks_found_txt),
            style = MaterialTheme.typography.bodyMedium
        )

    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyContentPrev() {
    TodoTheme {
        EmptyContent()
    }
}