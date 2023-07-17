package com.beran.todo.navigation.destination

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.beran.todo.ui.screen.list.ListScreen
import com.beran.todo.ui.viewmodel.SharedViewModel
import com.beran.todo.utils.Action
import com.beran.todo.utils.Constant.LIST_ARGUMENT_KEY
import com.beran.todo.utils.Constant.LIST_SCREEN
import com.beran.todo.utils.toAction


fun NavGraphBuilder.listComposable(
    navigateToTask: (Int) -> Unit,
    sharedViewModel: SharedViewModel,
) {
    composable(
        route = LIST_SCREEN,
        arguments = listOf(navArgument(name = LIST_ARGUMENT_KEY) {
            type = NavType.StringType
        }),
        content = { navBackStackEntry ->
            val action = navBackStackEntry.arguments?.getString(LIST_ARGUMENT_KEY).toAction()

            var myAction by rememberSaveable {
                mutableStateOf(Action.NO_ACTION)
            }

            LaunchedEffect(key1 = myAction, block = {
                if (action != myAction) {
                    myAction = action
                    sharedViewModel.updateAction(action)
                }
            })
            val databaseAction = sharedViewModel.action
            ListScreen(
                navigateToTask = navigateToTask,
                databaseAction = databaseAction,
                sharedViewModel = sharedViewModel
            )

        }
    )
}