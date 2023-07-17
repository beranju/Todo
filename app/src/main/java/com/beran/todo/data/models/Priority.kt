package com.beran.todo.data.models

import androidx.compose.ui.graphics.Color

enum class Priority(val color: Color) {
    High(Color.Red),
    Medium(Color.Yellow),
    Low(Color.Green),
    None(Color.White),
}