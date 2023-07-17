package com.beran.todo.ui.screen.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.beran.todo.ui.viewmodel.SharedViewModel
import com.beran.todo.utils.Action
import com.beran.todo.utils.SearchAppBarState
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf


@Composable
fun ListScreen(
    navigateToTask: (Int) -> Unit,
    databaseAction: Action,
    sharedViewModel: SharedViewModel
) {
    LaunchedEffect(key1 = databaseAction, block = {
        sharedViewModel.handleDatabaseAction(databaseAction)
    })

    val action = sharedViewModel.action
    val allTask by sharedViewModel.allTasks.collectAsState()
    val searchTask by sharedViewModel.searchTask.collectAsState()
    val sortState by sharedViewModel.sortState.collectAsState()
    val lowPriorityTasks by sharedViewModel.lowPriorityTasks.collectAsState()
    val highPriorityTasks by sharedViewModel.highPriorityTasks.collectAsState()
    val searchAppBarState: SearchAppBarState = sharedViewModel.searchAppBarState
    val searchTextState: String = sharedViewModel.searchTextState
    val snackBarHostState = remember { SnackbarHostState() }

    DisplaySnackBar(
        snackBarHostState = snackBarHostState,
        onComplete = { sharedViewModel.updateAction(it) },
        taskTitle = sharedViewModel.title,
        action = action,
        onUndoClicked = {
            sharedViewModel.updateAction(it)
        }
    )

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        topBar = {
            ListAppBar(
                sharedViewModel = sharedViewModel,
                searchAppBarState = searchAppBarState,
                searchTextState = searchTextState,
            )
        },
        floatingActionButton = {
            ListFab(navigateToTask = navigateToTask)
        },
        content = { contentPadding ->
            Box(modifier = Modifier.padding(contentPadding)) {
                ListContent(
                    allTasks = allTask,
                    searchTasks = searchTask,
                    searchAppBarState = searchAppBarState,
                    lowPriorityTask = lowPriorityTasks,
                    highPriorityTask = highPriorityTasks,
                    sortState = sortState,
                    navigateToTask = navigateToTask,
                    onSwipeToDelete = { action, task ->
                        sharedViewModel.updateAction(action)
                        sharedViewModel.updateTask(task)
                        snackBarHostState.currentSnackbarData?.dismiss()
                    }
                )
            }
        }
    )
}

@Composable
fun ListFab(
    navigateToTask: (Int) -> Unit
) {
    FloatingActionButton(onClick = {
        navigateToTask(-1)
    }) {
        Icon(imageVector = Icons.Default.Add, contentDescription = null)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplaySnackBar(
    snackBarHostState: SnackbarHostState,
    onComplete: (Action) -> Unit,
    onUndoClicked: (Action) -> Unit,
    taskTitle: String,
    action: Action
) {
    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = action, block = {
        if (action != Action.NO_ACTION) {
            scope.launch {
                val snackBarResult = snackBarHostState.showSnackbar(
                    message = setMessage(action, taskTitle),
                    actionLabel = setActionLabel(action)
                )
                undoDeletedTask(
                    action = action,
                    snackBarResult = snackBarResult,
                    onUndoClicked = onUndoClicked
                )
            }
            onComplete(Action.NO_ACTION)
        }
    })
}

private fun setMessage(action: Action, taskTitle: String): String {
    return when (action) {
        Action.DELETE_ALL -> "All tasks removed."
        else -> "${action.name}: $taskTitle"
    }
}

private fun setActionLabel(action: Action): String {
    return if (action.name == "DELETE") {
        "UNDO"
    } else {
        "OK"
    }
}

private fun undoDeletedTask(
    action: Action,
    snackBarResult: SnackbarResult,
    onUndoClicked: (Action) -> Unit
) {
    if (snackBarResult == SnackbarResult.ActionPerformed && action == Action.DELETE) {
        onUndoClicked(Action.UNDO)
    }
}

//@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
//@Composable
//private fun ListScreenPrev() {
//    TodoTheme {
//        ListScreen(navigateToTask = {}, sharedViewModel = viewModel())
//    }
//}