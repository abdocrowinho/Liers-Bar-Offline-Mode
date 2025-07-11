package com.example.liersbarofflinemode.ui.composable.gunScreen.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.liersbarofflinemode.R
import com.example.liersbarofflinemode.ui.Utltiy.GetHeightConf
import com.example.liersbarofflinemode.ui.Utltiy.GetWidthConf

@Composable
fun AnimatedBullet(modifier: Modifier, xOffset: Dp){
    Image(painter = painterResource(id = R.drawable.bullet_left_destention)
        , contentDescription ="fire bullet"
        , contentScale = ContentScale.FillBounds,
        modifier = modifier

            .width(GetWidthConf() * .03f)
            .height(GetHeightConf() * .05f)
            .offset(x = xOffset, y = (-50).dp)

    )
}