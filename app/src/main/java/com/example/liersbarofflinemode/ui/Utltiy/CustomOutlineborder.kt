package com.example.liersbarofflinemode.ui.Utltiy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.example.liersbarofflinemode.ui.theme.dark_red
import com.example.liersbarofflinemode.ui.theme.red_orange
import com.example.liersbarofflinemode.ui.theme.warm_peach

@Composable
fun CustomOutLineBorder(
    modifier: Modifier = Modifier,
    width: Dp,
    radius: Dp,
    color: Color,
    text:String,
    onTextChange:(String) -> Unit,
    isError: Boolean ,
    supportingText: @Composable (() -> Unit)?=null
) {
Column {
    OutlinedTextField(
        value = text, onValueChange = { newText -> onTextChange(newText) },
        modifier
            .width(width)
            .wrapContentSize()
            .background(red_orange, shape = RoundedCornerShape(radius)),
        shape = RoundedCornerShape(radius),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = warm_peach,
            cursorColor = warm_peach,
            focusedTextColor = warm_peach,
            disabledBorderColor = dark_red
        ),
        isError = isError,
    )
    supportingText?.invoke()

}



}
