package com.beran.todo.ui.screen.task

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.beran.todo.data.models.Priority
import com.beran.todo.data.models.TodoTask
import com.beran.todo.ui.viewmodel.SharedViewModel
import com.beran.todo.utils.Action

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    selectedTask: TodoTask?,
    sharedViewModel: SharedViewModel,
    navigateToListTask: (Action) -> Unit
) {
    val context = LocalContext.current
    val title = sharedViewModel.title
    val description = sharedViewModel.description
    val priority = sharedViewModel.priority
    LaunchedEffect(key1 = selectedTask, block = {
        if (selectedTask != null) {
            sharedViewModel.updateId(selectedTask.id)
        }
    })

//    BackHandler(onBackPressed = {
//        navigateToListTask(Action.NO_ACTION)
//    })
    // **  fun to handle back press from android docs
    BackHandler {
        navigateToListTask(Action.NO_ACTION)
    }

    Scaffold(
        topBar = {
            TaskAppBar(selectedTask = selectedTask, navigateToListTask = { action ->
                if (action == Action.NO_ACTION) {
                    navigateToListTask(action)
                } else {
                    if (sharedViewModel.validateFields()) {
                        navigateToListTask(action)
                    } else {
                        displayToast(context)
                    }
                }
            })
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            TaskContent(
                title = title,
                onTitleChange = { sharedViewModel.updateTitle(it) },
                priority = priority,
                onPriorityChange = { sharedViewModel.updatePriority(it) },
                description = description,
                onChangeDescription = { sharedViewModel.updateDesc(it) }
            )
        }
    }
}

fun displayToast(context: Context) {
    Toast.makeText(context, "Field Empty", Toast.LENGTH_SHORT).show()
}

/**
 * this function use to intercept back button
 * mengatasi bug saat membuat task
 */
//@Composable
//fun BackHandler(
//    backDispatcher: OnBackPressedDispatcher? = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher,
//    onBackPressed: () -> Unit,
//) {
//    val currentOnBackPressed by rememberUpdatedState(newValue = onBackPressed)
//
//    val backCallBack = remember {
//        object : OnBackPressedCallback(enabled = true) {
//            override fun handleOnBackPressed() {
//                currentOnBackPressed()
//            }
//        }
//    }
//
//    DisposableEffect(key1 = backDispatcher, effect = {
//        backDispatcher?.addCallback(backCallBack)
//        onDispose {
//            backCallBack.remove()
//        }
//    })
//
//}
