//package com.beran.todo.ui.screen.splash
//
//import android.content.res.Configuration.UI_MODE_NIGHT_YES
//import androidx.compose.animation.core.animateDpAsState
//import androidx.compose.animation.core.animateFloatAsState
//import androidx.compose.animation.core.tween
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.isSystemInDarkTheme
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.offset
//import androidx.compose.foundation.layout.size
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.alpha
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.Dp
//import androidx.compose.ui.unit.dp
//import com.beran.todo.R
//import com.beran.todo.ui.theme.LOGO_HIGH
//import com.beran.todo.ui.theme.TodoTheme
//import com.beran.todo.ui.theme.splashBackGround
//import kotlinx.coroutines.delay
//
//@Composable
//fun SplashScreen(
//    navigateToListScreen: () -> Unit
//) {
//    var startAnim by remember {
//        mutableStateOf(false)
//    }
//    val offsetState by animateDpAsState(
//        targetValue = if (startAnim) 0.dp else 100.dp,
//        animationSpec = tween(1000)
//    )
//    val alphaState by animateFloatAsState(
//        targetValue = if (startAnim) 1f else 0f,
//        animationSpec = tween(1000)
//    )
//
//    LaunchedEffect(key1 = true, block = {
//        startAnim = true
//        delay(3000L)
//        navigateToListScreen()
//    })
//
//    SplashContent(offsetState, alphaState)
//}
//
//@Composable
//fun SplashContent(
//    offsetState: Dp,
//    alphaState: Float
//) {
//    Box(
//        contentAlignment = Alignment.Center,
//        modifier = Modifier
//            .fillMaxSize()
//            .background(MaterialTheme.colorScheme.splashBackGround)
//    ) {
//        Image(
//            painter = painterResource(id = getLogo()),
//            contentDescription = stringResource(R.string.app_logo),
//            modifier = Modifier
//                .size(LOGO_HIGH)
//                .offset(y = offsetState)
//                .alpha(alphaState)
//        )
//    }
//}
//
//@Composable
//fun getLogo(): Int {
//    return if (isSystemInDarkTheme()) R.drawable.logo_dark else R.drawable.logo_light
//}
//
//@Preview(showBackground = true)
//@Composable
//fun SplashScreenPrev() {
//    TodoTheme {
//        SplashScreen {}
//    }
//}
//
//@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
//@Composable
//fun SplashScreenPrevDark() {
//    TodoTheme {
//        SplashScreen {}
//    }
//}