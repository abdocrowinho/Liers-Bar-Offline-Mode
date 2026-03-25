package com.example.liersbarofflinemode.ui.composable.LanGamePlay.Composable

import LanPlayerAvatar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.domain.Entitys.LanUserEntity
import com.example.liersbarofflinemode.ui.ViewModels.LanGamePlayViewModel

@Composable
fun HorizontalPlayers(
    modifier: Modifier,
    rightPlayer: LanUserEntity?,
    leftPlayer: LanUserEntity?,
    currentTurnId: Int,
    isMyTurn: Boolean,
    viewModel: LanGamePlayViewModel
) {
    Box(modifier = modifier.fillMaxSize().padding(horizontal = 40.dp)) {
        rightPlayer?.let {
            LanPlayerAvatar(
                rotate = 90f,
                playerState = it,
                modifier = Modifier.align(Alignment.CenterEnd),
                size = 60.dp ,
                currentTurnId = currentTurnId,
                viewModel = viewModel,
                timerValue = if (isMyTurn) 10 else null

            )
        }
        leftPlayer?.let {
            LanPlayerAvatar(
                rotate = 270f,
                playerState = it,
                modifier = Modifier.align(Alignment.CenterStart),
                size = 60.dp,
                currentTurnId = currentTurnId,
                viewModel = viewModel,
                timerValue = if (isMyTurn) 10 else null

            )
        }
    }
}