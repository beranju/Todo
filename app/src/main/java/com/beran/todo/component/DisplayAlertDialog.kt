package com.beran.todo.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.beran.todo.R

@Composable
fun DisplayAlertDialog(
    title: String,
    message: String,
    openDialog: Boolean,
    closeDialog: () -> Unit,
    onYesClicked: () -> Unit
) {
    if (openDialog) {
        AlertDialog(
            title = { Text(text = title, style = MaterialTheme.typography.titleSmall) },
            text = {
                Text(text = message, style = MaterialTheme.typography.bodyMedium)
            },
            onDismissRequest = closeDialog,
            confirmButton = {
                Button(onClick = {
                    onYesClicked()
                    closeDialog()
                }) {
                    Text(text = stringResource(R.string.yes_txt))
                }
            },
            dismissButton = {
                OutlinedButton(onClick = {
                    closeDialog()
                }) {
                    Text(text = stringResource(R.string.no_txt))
                }
            }
        )
    }
}