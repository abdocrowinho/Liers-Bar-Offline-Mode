package com.example.liersbarofflinemode.ui.theme.mainActivity.ui.Bases.Screens.MainActivity.Composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.liersbarofflinemode.R
import com.example.liersbarofflinemode.ui.Utltiy.GetHeightConf
import com.example.liersbarofflinemode.ui.Utltiy.GetWidthConf

@Composable
fun Button(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
width : Float
    ,height:Float
) {
    Box(
        modifier = modifier
            .width(GetWidthConf() * width)
            .height(GetHeightConf() * height)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.main_button), contentDescription = "asdf",
            contentScale = ContentScale.FillBounds,
            modifier = modifier.matchParentSize()
        )

        Text(text = text
        ,modifier.align(Alignment.Center)
        )

    }


}