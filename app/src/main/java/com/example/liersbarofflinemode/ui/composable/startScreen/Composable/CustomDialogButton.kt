package com.example.liersbarofflinemode.ui.composable.startScreen.Composable

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.liersbarofflinemode.ui.Utltiy.GetHeightConf
import com.example.liersbarofflinemode.ui.Utltiy.GetWidthConf
import com.example.liersbarofflinemode.ui.theme.red_orange

@Composable
fun CustomContainerButton(
    text : String,
    modifier: Modifier?= Modifier,
    onClick : ()-> Unit ,
){
    if (modifier != null) {
        Button(onClick = { onClick()  } ,
            shape = RectangleShape ,
                modifier = modifier.width(GetWidthConf() * .2f)
                .height(GetHeightConf()*.1f)
        ) {
            Text(text = text)
        }
    }
}
