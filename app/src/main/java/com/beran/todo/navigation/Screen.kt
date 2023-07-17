package com.beran.todo.navigation

import androidx.navigation.NavHostController
import com.beran.todo.utils.Action
import com.beran.todo.utils.Constant.LIST_SCREEN
import com.beran.todo.utils.Constant.SPLASH_SCREEN

class Screen(navController: NavHostController) {
    val splash: () -> Unit = {
        navController.navigate("list/${Action.NO_ACTION.name}") {
            popUpTo(SPLASH_SCREEN) { inclusive = true }
        }
    }
    val task: (Action) -> Unit = { action ->
        navController.navigate("list/${action.name}") {
            popUpTo(LIST_SCREEN) {
                inclusive = true
            }
        }
    }

    val list: (Int) -> Unit = { taskId ->
        navController.navigate("task/$taskId")
    }
}