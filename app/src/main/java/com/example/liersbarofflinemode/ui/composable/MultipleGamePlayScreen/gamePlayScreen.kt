package com.example.liersbarofflinemode.ui.composable.MultipleGamePlayScreen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.domain.Entitys.UserEntity
import com.example.liersbarofflinemode.R
import com.example.liersbarofflinemode.ui.States.GunShotState
import com.example.liersbarofflinemode.ui.States.LastBulletInGame
import com.example.liersbarofflinemode.ui.Utltiy.ArgumentsKeys
import com.example.liersbarofflinemode.ui.ViewModels.GamePlayViewModel
import com.example.liersbarofflinemode.ui.composable.MultipleGamePlayScreen.Composable.PLayerInX
import com.example.liersbarofflinemode.ui.composable.MultipleGamePlayScreen.Composable.PlayersInY
import com.example.liersbarofflinemode.ui.composable.endGameDialog.ShowWinnerDialog

@Composable
fun GamePlayScreen(navHostController: NavHostController,
                   viewModel: GamePlayViewModel = hiltViewModel()) {


    val gunScreenState by  viewModel.gunScreenState.collectAsState()

    val players = navHostController.previousBackStackEntry
        ?.savedStateHandle?.get<List<UserEntity>>(ArgumentsKeys.PLAYERS_KEY)

    if (players != null && players.size == 4) {
        viewModel.setPlayers(players)
    } else {
        Log.e("GamePlayScreen", "Invalid players list")
    }
LaunchedEffect(Unit) {
    viewModel.clearGunShotState()
}

    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painter = painterResource(id = R.drawable.game_play_background),
                contentScale = ContentScale.FillBounds
            )
    ) {

        PLayerInX(
            leftUserState = viewModel.playersState[2]!!,
            rightUserState = viewModel.playersState[4]!!,
            navHostController = navHostController,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
        )

        PlayersInY(
            playerStateTop = viewModel.playersState[1]!!,
            playerStateBottom = viewModel.playersState[3]!!,
            navHostController = navHostController,
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.Center),
        )
        when(gunScreenState){
            is LastBulletInGame ->
                ShowWinnerDialog((gunScreenState as LastBulletInGame).playerWinner,
                    modifier = Modifier.align(Alignment.Center), navController = navHostController){
                    viewModel.zeroingDeadCounter()
                }

            is GunShotState -> Image(painter = painterResource(id = R.drawable.gun)
                , contentDescription ="gun for table", modifier = Modifier.align(Alignment.Center).size(20.dp) )
            null -> Image(painter = painterResource(id = R.drawable.gun)
                , contentDescription ="gun for table", modifier = Modifier.align(Alignment.Center).size(20.dp) )
        }



    }

}

