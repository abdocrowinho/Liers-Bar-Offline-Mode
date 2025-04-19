package com.example.liersbarofflinemode.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

data class TextFieldModel (
    val text : String,
    val onTextChange : (String)->Unit,
    val width :Dp,
    val radius:Dp,
    val color: Color
)