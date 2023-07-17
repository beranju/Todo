package com.beran.todo.navigation.destination

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideOutVertically
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.beran.todo.utils.Constant.SPLASH_SCREEN


//fun NavGraphBuilder.splashComposable(
//    navigateToListScreen: () -> Unit,
//) {
//    composable(
//        route = SPLASH_SCREEN,
//        exitTransition = {
//            slideOutVertically(targetOffsetY = { -it }, animationSpec = tween(durationMillis = 300))
//        },
//        content = {
//            SplashScreen(navigateToListScreen = navigateToListScreen)
//        }
//    )
//}