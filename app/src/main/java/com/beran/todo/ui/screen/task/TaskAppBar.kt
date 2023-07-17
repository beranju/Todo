package com.beran.todo.ui.screen.task

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Upgrade
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.beran.todo.R
import com.beran.todo.component.DisplayAlertDialog
import com.beran.todo.data.models.Priority
import com.beran.todo.data.models.TodoTask
import com.beran.todo.ui.theme.TodoTheme
import com.beran.todo.utils.Action

@Composable
fun TaskAppBar(
    selectedTask: TodoTask?,
    navigateToListTask: (Action) -> Unit
) {
    if (selectedTask == null) {
        NewTaskAppBar(navigateToListTask = navigateToListTask)
    } else {
        ExistingTaskAppBar(selectedTask = selectedTask, navigateToListTask = navigateToListTask)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTaskAppBar(
    navigateToListTask: (Action) -> Unit
) {
    TopAppBar(
        navigationIcon = {
            BackAction(onBackClicked = navigateToListTask)
        },
        title = {
            Text(
                text = stringResource(R.string.add_task_hint),
                color = MaterialTheme.colorScheme.background
            )
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
        actions = {
            AddAction(onAddClicked = navigateToListTask)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExistingTaskAppBar(
    selectedTask: TodoTask,
    navigateToListTask: (Action) -> Unit
) {
    TopAppBar(
        navigationIcon = {
            CloseAction(onCloseClicked = navigateToListTask)
        },
        title = {
            Text(
                text = selectedTask.title,
                color = MaterialTheme.colorScheme.background,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
        actions = {
            ExistingTaskAppBarActions(
                selectedTask = selectedTask,
                navigateToListTask = navigateToListTask
            )
        }
    )
}

@Composable
fun ExistingTaskAppBarActions(
    selectedTask: TodoTask,
    navigateToListTask: (Action) -> Unit
) {
    var openDialog by remember {
        mutableStateOf(false)
    }

    DisplayAlertDialog(
        title = stringResource(R.string.remove_task, selectedTask.title),
        message = stringResource(id = R.string.remove_task_confirmation, selectedTask.title),
        openDialog = openDialog,
        closeDialog = { openDialog = false },
        onYesClicked = {
            navigateToListTask(Action.DELETE)
        }
    )

    DeleteAction(onDeleteClicked = {
        openDialog = true
    })
    UpdateAction(onUpdateClicked = navigateToListTask)

}

@Composable
fun BackAction(
    onBackClicked: (Action) -> Unit
) {
    IconButton(onClick = { onBackClicked(Action.NO_ACTION) }) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = stringResource(R.string.back_hint),
            tint = MaterialTheme.colorScheme.background
        )
    }
}

@Composable
fun CloseAction(
    onCloseClicked: (Action) -> Unit
) {
    IconButton(onClick = { onCloseClicked(Action.NO_ACTION) }) {
        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = stringResource(R.string.close_txt),
            tint = MaterialTheme.colorScheme.background
        )
    }
}

@Composable
fun AddAction(
    onAddClicked: (Action) -> Unit
) {
    IconButton(onClick = { onAddClicked(Action.ADD) }) {
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = stringResource(R.string.add_task_hint),
            tint = MaterialTheme.colorScheme.background
        )
    }
}

@Composable
fun DeleteAction(
    onDeleteClicked: () -> Unit
) {
    IconButton(onClick = { onDeleteClicked() }) {
        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = stringResource(R.string.delete_task),
            tint = MaterialTheme.colorScheme.background
        )
    }
}

@Composable
fun UpdateAction(
    onUpdateClicked: (Action) -> Unit
) {
    IconButton(onClick = { onUpdateClicked(Action.UPDATE) }) {
        Icon(
            imageVector = Icons.Filled.Upgrade,
            contentDescription = stringResource(R.string.update_icon),
            tint = MaterialTheme.colorScheme.background
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AddTaskAppBar() {
    TodoTheme {
        NewTaskAppBar(navigateToListTask = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun ExitingTaskAppBarPrev() {
    TodoTheme {
        ExistingTaskAppBar(
            selectedTask = TodoTask(1, "Halo", "descroptiom", Priority.Low),
            navigateToListTask = {})
    }
}