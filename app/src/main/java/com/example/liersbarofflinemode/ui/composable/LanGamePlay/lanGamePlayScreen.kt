package com.example.liersbarofflinemode.ui.composable.LanGamePlay

import LanPlayerAvatar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.domain.Utlites.getMyIpAddress
import com.example.liersbarofflinemode.R
import com.example.liersbarofflinemode.ui.ViewModels.LanGamePlayViewModel
import com.example.liersbarofflinemode.ui.composable.LanGamePlay.Composable.OurCard

@Composable
fun LanGamePlayScreen(
    navController: NavHostController,
    lanGamePlayViewModel: LanGamePlayViewModel
) {
    val playersState by lanGamePlayViewModel.players.collectAsState()

    val myIp = getMyIpAddress()
    val currentPlayer = playersState?.find { it?.ipAddress == myIp }

    val allPlayers = playersState?.filterNotNull() ?: emptyList()

    val ids = allPlayers.map { it.id }.sorted()

    currentPlayer?.let { me ->

        val myIndex = ids.indexOf(me.id)
        val rightId = ids.getOrNull((myIndex + 1) % 4)
        val oppositeId = ids.getOrNull((myIndex + 2) % 4)
        val leftId = ids.getOrNull((myIndex + 3) % 4)

        val rightPlayer = allPlayers.find { it.id == rightId }
        val oppositePlayer = allPlayers.find { it.id == oppositeId }
        val leftPlayer = allPlayers.find { it.id == leftId }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .paint(
                    painter = painterResource(id = R.drawable.game_play_background),
                    contentScale = ContentScale.FillBounds
                )
        ) {
            LanPlayerAvatar(
                rotate = 0f,
                playerState = me,
                modifier = Modifier.offset(0.dp, 0.dp),
                size = 70.dp
            )

            Column(
                Modifier
                    .fillMaxHeight()
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                oppositePlayer?.let {
                    LanPlayerAvatar(rotate = 0f, playerState = it, modifier = Modifier, size = 60.dp)
                }
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                rightPlayer?.let {
                    LanPlayerAvatar(rotate = 180f, playerState = it, modifier = Modifier, size = 60.dp)
                }
                leftPlayer?.let {
                    LanPlayerAvatar(rotate = 0f, playerState = it, modifier = Modifier, size = 60.dp)
                }

            }
            LazyRow( modifier = Modifier.align(Alignment.BottomCenter)) {
                items(currentPlayer.cards.size ){i->
                    OurCard( card = currentPlayer.cards[i] , viewModel = lanGamePlayViewModel )
                }
            }




            }

    }
}