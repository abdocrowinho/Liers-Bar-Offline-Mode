package com.example.liersbarofflinemode.ui.composable.LanGamePlay.Composable

import LanPlayerAvatar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.domain.Entitys.LanUserEntity
import com.example.liersbarofflinemode.ui.Utltiy.GetHeightConf

@Composable
fun VerticalPlayer(modifier: Modifier , oppositePlayer:LanUserEntity?) {
    Column(
        Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(top = GetHeightConf()*.08f), horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        oppositePlayer?.let {
            LanPlayerAvatar(rotate = 0f, playerState = it, modifier = Modifier, size = 60.dp)
        }
    }
}