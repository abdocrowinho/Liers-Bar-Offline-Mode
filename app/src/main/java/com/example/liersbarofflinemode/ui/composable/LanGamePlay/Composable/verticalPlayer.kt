package com.example.liersbarofflinemode.ui.composable.LanGamePlay.Composable

import LanPlayerAvatar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.domain.Entitys.LanUserEntity
import com.example.liersbarofflinemode.ui.Utltiy.GetHeightConf
import com.example.liersbarofflinemode.ui.ViewModels.LanGamePlayViewModel


@Composable
fun VerticalPlayer(modifier: Modifier, oppositePlayer: LanUserEntity?,
                   isMyTurn: Boolean,
                   currentTurnId: Int,
                   viewModel: LanGamePlayViewModel
                   ) {
    Box(modifier = modifier.fillMaxSize().padding(top = 40.dp)) {
        oppositePlayer?.let {
            LanPlayerAvatar(
                rotate = 0f,
                playerState = it,
                modifier = Modifier.align(Alignment.TopCenter),
                size = 60.dp,
                timerValue = if (isMyTurn) 10 else null,
                currentTurnId = currentTurnId,
                viewModel = viewModel


            )
        }
    }
}