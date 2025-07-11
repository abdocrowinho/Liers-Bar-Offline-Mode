package com.example.liersbarofflinemode.ui.composable.gunScreen.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.domain.Entitys.UserEntity
import com.example.liersbarofflinemode.R
import com.example.liersbarofflinemode.ui.Utltiy.GetHeightConf
import com.example.liersbarofflinemode.ui.Utltiy.GetWidthConf
import kotlinx.coroutines.flow.StateFlow

@Composable
fun NumberBulletsRow( modifier: Modifier , playerState:StateFlow<UserEntity?>?){
    LazyRow(horizontalArrangement = Arrangement.Center , modifier = modifier ){
        items(playerState?.value?.remainingBullets ?: 0) {
            Image(
                painter = painterResource(id = R.drawable.bullet_top_destention),
                contentDescription = "Bullet",
                modifier = modifier
                    .width(GetWidthConf() * .025f)
                    .height(GetHeightConf() * .13f),
                contentScale = ContentScale.FillBounds
            )

        }
    }
}