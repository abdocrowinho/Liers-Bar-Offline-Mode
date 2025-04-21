package com.example.liersbarofflinemode.ui.models

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

data class TextFieldModel (
    val text : String,
    val onTextChange : (String)->Unit,
    val isError:Boolean,
    val supportingText:@Composable (()->Unit)?=null
)