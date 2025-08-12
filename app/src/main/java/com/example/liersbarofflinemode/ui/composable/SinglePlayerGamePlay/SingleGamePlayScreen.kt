
package com.example.liersbarofflinemode.ui.composable.SinglePlayerGamePlay

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import com.example.liersbarofflinemode.R
import com.example.liersbarofflinemode.ui.States.GunScreenState
import com.example.liersbarofflinemode.ui.States.GunShotState
import com.example.liersbarofflinemode.ui.States.LastBulletInGame
import com.example.liersbarofflinemode.ui.ViewModels.EnterSingleUserViewModel
import com.example.liersbarofflinemode.ui.composable.MultipleGamePlayScreen.Composable.PlayerAvatar
import com.example.liersbarofflinemode.ui.composable.endGameDialog.ShowWinnerDialog
import com.example.liersbarofflinemode.ui.theme.mainActivity.ui.Bases.NavigationItem

@Composable
fun SingleGamePlayScreen(
    navHostController: NavHostController,
    viewModel: EnterSingleUserViewModel = hiltViewModel()
) {
    val gunScreenState by viewModel.gunScreenState.collectAsState()


    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painter = painterResource(id = R.drawable.game_play_background),
                contentScale = ContentScale.FillBounds
            )
    ) {

        Column(modifier = Modifier.align(Alignment.TopCenter)) {
            PlayerAvatar(rotate = 0f, playerState = viewModel.playerState) {
                navHostController.navigate(NavigationItem.SingleGunScreen.route)
            }
        }


        when (gunScreenState) {
            is LastBulletInGame ->
                ShowWinnerDialog(
                    playerWinner = (gunScreenState as LastBulletInGame).playerWinner,
                    modifier = Modifier.align(Alignment.Center), navController = navHostController,
                    text = "Game Over , ${(gunScreenState as LastBulletInGame).playerWinner[0].name}"
                    , hasUserName = false
                ) {
                    Log.d("endGameDialog", "EndGamDialog Showing: ")
                }

            is GunShotState -> Image(painter = painterResource(id = R.drawable.gun)
                , contentDescription ="gun for table", modifier = Modifier.align(Alignment.Center).size(20.dp) )
            null -> Image(painter = painterResource(id = R.drawable.gun)
                , contentDescription ="gun for table", modifier = Modifier.align(Alignment.Center).size(100.dp) )
        }
    }
}