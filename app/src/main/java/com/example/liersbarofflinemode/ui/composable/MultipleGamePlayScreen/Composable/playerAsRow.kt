package com.example.liersbarofflinemode.ui.composable.MultipleGamePlayScreen.Composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.domain.Entitys.UserEntity
import com.example.liersbarofflinemode.ui.theme.mainActivity.ui.Bases.NavigationItem
import kotlinx.coroutines.flow.StateFlow

@Composable
fun PLayerInX(
    leftUserState: StateFlow<UserEntity?>, rightUserState: StateFlow<UserEntity?>,
    navHostController: NavController, modifier: Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {


        PlayerAvatar(
            playerState = leftUserState,
            rotate = -75f,
        ) {

            navHostController.navigate("${NavigationItem.GunScreen.route}/2") {
                launchSingleTop = true

            }
        }


        PlayerAvatar(
            playerState = rightUserState,
            rotate = 75f,
        ) {
            navHostController.navigate("${NavigationItem.GunScreen.route}/4") {
                launchSingleTop = true

            }
        }
    }
}