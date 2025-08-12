package com.example.liersbarofflinemode.ui.Utltiy

fun provideColorToGunScreen() : androidx.compose.ui.graphics.Color {
    val numberColor = (1..6).random()
    return   when(numberColor){
        1-> androidx.compose.ui.graphics.Color.Cyan
        2-> androidx.compose.ui.graphics.Color.Green
        3-> androidx.compose.ui.graphics.Color.Black
        4-> androidx.compose.ui.graphics.Color.Red
        5-> androidx.compose.ui.graphics.Color.Gray
        6-> androidx.compose.ui.graphics.Color.Blue
        else -> androidx.compose.ui.graphics.Color.Green
    }
}