package com.beran.todo.ui.screen.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.beran.todo.R
import com.beran.todo.data.models.Priority
import com.beran.todo.data.models.TodoTask
import com.beran.todo.ui.theme.LARGEST_PADDING
import com.beran.todo.ui.theme.LARGE_PADDING
import com.beran.todo.ui.theme.MEDIUM_PADDING
import com.beran.todo.ui.theme.PRIORITY_INDICATOR_SIZE
import com.beran.todo.ui.theme.TASK_ITEM_ELEVATION
import com.beran.todo.ui.theme.TodoTheme
import com.beran.todo.utils.Action
import com.beran.todo.utils.RequestState
import com.beran.todo.utils.SearchAppBarState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ListContent(
    allTasks: RequestState<List<TodoTask>>,
    searchTasks: RequestState<List<TodoTask>>,
    searchAppBarState: SearchAppBarState,
    lowPriorityTask: List<TodoTask>,
    highPriorityTask: List<TodoTask>,
    sortState: RequestState<Priority>,
    navigateToTask: (taskId: Int) -> Unit,
    onSwipeToDelete: (Action, TodoTask) -> Unit,
) {
    if (sortState is RequestState.Success) {
        when {
            searchAppBarState == SearchAppBarState.TRIGGERED -> {
                if (searchTasks is RequestState.Success) {
                    HandleTasks(
                        tasks = searchTasks.data,
                        navigateToTask = navigateToTask,
                        onSwipeToDelete = onSwipeToDelete,
                    )
                }
            }

            sortState.data == Priority.None -> {
                if (allTasks is RequestState.Success) {
                    HandleTasks(
                        tasks = allTasks.data,
                        onSwipeToDelete = onSwipeToDelete,
                        navigateToTask = navigateToTask
                    )
                }
            }

            sortState.data == Priority.Low -> {
                HandleTasks(
                    tasks = lowPriorityTask,
                    onSwipeToDelete = onSwipeToDelete,
                    navigateToTask = navigateToTask
                )
            }

            sortState.data == Priority.High -> {
                HandleTasks(
                    tasks = highPriorityTask,
                    onSwipeToDelete = onSwipeToDelete,
                    navigateToTask = navigateToTask
                )
            }
        }
    }
}

@Composable
fun HandleTasks(
    tasks: List<TodoTask>,
    onSwipeToDelete: (Action, TodoTask) -> Unit,
    navigateToTask: (taskId: Int) -> Unit
) {
    if (tasks.isEmpty()) {
        EmptyContent()
    } else {
        DisplayTasks(
            listToDoTask = tasks,
            onSwipeToDelete = onSwipeToDelete,
            navigateToTask = navigateToTask
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayTasks(
    listToDoTask: List<TodoTask>,
    onSwipeToDelete: (Action, TodoTask) -> Unit,
    navigateToTask: (taskId: Int) -> Unit
) {
    LazyColumn {
        items(listToDoTask, key = { it.id }) { item ->
            val dismissState = rememberDismissState()
            val dismissDirection = dismissState.dismissDirection
            val isDismiss = dismissState.isDismissed(DismissDirection.EndToStart)
            if (isDismiss && dismissDirection == DismissDirection.EndToStart) {
                /**
                 * coroutine scope digunakan untuk memberikan delay sebelum fungsi onSwipeDelete()...
                 * ...dijalanakan sehingga animasi enter berhasil ditampilkkan pada item yang di hapus
                 */
                val scope = rememberCoroutineScope()
                scope.launch {
                    delay(300)
                    onSwipeToDelete(Action.DELETE, item)
                }
            }
            val degrees by animateFloatAsState(targetValue = if (dismissState.targetValue == DismissValue.Default) 0f else -45f)
            var itemAppeared by remember {
                mutableStateOf(false)
            }
            LaunchedEffect(key1 = true, block = { itemAppeared = true })

            AnimatedVisibility(
                visible = itemAppeared && !isDismiss,
                enter = expandVertically(animationSpec = tween(300)),
                exit = shrinkVertically(animationSpec = tween(300))
            ) {
                SwipeToDismiss(
                    state = dismissState,
                    directions = setOf(DismissDirection.EndToStart),
                    background = { RedBackGround(degrees = degrees) },
                    dismissContent = {
                        TaskItem(todoTask = item, navigateToTask = navigateToTask)
                    })
            }
        }
    }
}

@Composable
fun RedBackGround(degrees: Float) {
    Box(
        contentAlignment = Alignment.CenterEnd,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red)
            .padding(horizontal = LARGEST_PADDING)
    ) {
        Icon(
            modifier = Modifier.rotate(degrees),
            imageVector = Icons.Filled.Delete,
            contentDescription = stringResource(R.string.delete_hint),
            tint = Color.White
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskItem(
    todoTask: TodoTask,
    navigateToTask: (taskId: Int) -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RectangleShape,
        shadowElevation = TASK_ITEM_ELEVATION,
        onClick = {
            navigateToTask(todoTask.id)
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(LARGE_PADDING)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(MEDIUM_PADDING),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = todoTask.title,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )
                Box {
                    Canvas(modifier = Modifier
                        .size(PRIORITY_INDICATOR_SIZE),
                        onDraw = {
                            drawCircle(
                                color = todoTask.priority.color
                            )
                        }
                    )
                }

            }
            Text(
                text = todoTask.description,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
private fun TaskItemPrev() {
    TodoTheme {
        TaskItem(
            todoTask = TodoTask(
                id = 1,
                title = "Belajar Kotlin",
                description = "Belajar di udemy bersama stefan",
                priority = Priority.Medium
            ), navigateToTask = {})
    }
}