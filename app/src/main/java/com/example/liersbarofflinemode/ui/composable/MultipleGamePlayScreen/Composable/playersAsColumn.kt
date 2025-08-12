package com.example.liersbarofflinemode.ui.composable.MultipleGamePlayScreen.Composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.domain.Entitys.UserEntity
import com.example.liersbarofflinemode.ui.theme.mainActivity.ui.Bases.NavigationItem
import kotlinx.coroutines.flow.StateFlow

@Composable
fun PlayersInY(playerStateTop:StateFlow<UserEntity?>,
               playerStateBottom:StateFlow<UserEntity?>,
               navHostController:NavController,
               modifier: Modifier
                   ){
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        PlayerAvatar(
            playerState = playerStateTop,
            rotate = -3f,
        ) {
            navHostController.navigate("${NavigationItem.GunScreen.route}/1"){
                launchSingleTop = true

            }
        }


        PlayerAvatar(
            playerState = playerStateBottom,
            rotate = 0f,
        ) {
            navHostController.navigate("${NavigationItem.GunScreen.route}/3"){
                launchSingleTop = true

            }
        }

    }
}