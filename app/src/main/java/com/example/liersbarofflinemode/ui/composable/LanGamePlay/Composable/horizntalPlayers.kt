package com.example.liersbarofflinemode.ui.composable.LanGamePlay.Composable

import LanPlayerAvatar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.domain.Entitys.LanUserEntity

@Composable
fun HorizontalPlayers(modifier: Modifier, rightPlayer: LanUserEntity?, leftPlayer: LanUserEntity?) {
    Row(horizontalArrangement = Arrangement.SpaceBetween) {
        rightPlayer?.let {
            LanPlayerAvatar(rotate = 0f, playerState = it, modifier = Modifier, size = 60.dp)

        }
        leftPlayer?.let {
            LanPlayerAvatar(rotate = 0f, playerState = it, modifier = Modifier, size = 60.dp)

        }
    }
}
