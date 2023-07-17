package com.beran.todo.navigation.destination

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.beran.todo.ui.screen.task.TaskScreen
import com.beran.todo.ui.viewmodel.SharedViewModel
import com.beran.todo.utils.Action
import com.beran.todo.utils.Constant
import com.beran.todo.utils.Constant.TASK_ARGUMENT_KEY

fun NavGraphBuilder.taskComposable(
    sharedViewModel: SharedViewModel,
    navigateToList: (Action) -> Unit,
) {
    composable(
        route = Constant.TASK_SCREEN,
        arguments = listOf(navArgument(name = TASK_ARGUMENT_KEY) {
            type = NavType.IntType
        }),
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { width -> -width },
                animationSpec = tween(300)
            )
        },
        content = { navBackStackEntry ->
            val taskId = navBackStackEntry.arguments?.getInt(TASK_ARGUMENT_KEY) ?: -1
            LaunchedEffect(key1 = taskId, block = { sharedViewModel.getSelectedTask(taskId) })
            val selectedTask by sharedViewModel.selectedTask.collectAsState()
            LaunchedEffect(key1 = selectedTask, block = {
                if (selectedTask != null && taskId == -1) {
                    sharedViewModel.updateTask(selectedTask)
                }
            })
            TaskScreen(
                selectedTask = selectedTask,
                sharedViewModel = sharedViewModel,
                navigateToListTask = navigateToList
            )

        }
    )
}