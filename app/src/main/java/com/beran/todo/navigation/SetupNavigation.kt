package com.beran.todo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.beran.todo.navigation.destination.listComposable
import com.beran.todo.navigation.destination.taskComposable
import com.beran.todo.ui.viewmodel.SharedViewModel
import com.beran.todo.utils.Constant.LIST_SCREEN
import com.beran.todo.utils.Constant.SPLASH_SCREEN

@Composable
fun SetupNavigation(
    navController: NavHostController,
    sharedViewModel: SharedViewModel
) {
    val screen = remember(navController) {
        Screen(navController = navController)
    }

    NavHost(navController = navController, startDestination = LIST_SCREEN, builder = {
//        splashComposable(
//            navigateToListScreen = screen.splash
//        )
        listComposable(
            navigateToTask = screen.list,
            sharedViewModel = sharedViewModel
        )
        taskComposable(
            sharedViewModel = sharedViewModel,
            navigateToList = screen.task
        )
    })
}