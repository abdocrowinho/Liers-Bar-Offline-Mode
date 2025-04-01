package com.example.liersbarofflinemode.ui.composable.startScreen.Composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.liersbarofflinemode.ui.theme.mainActivity.ui.Bases.NavigationItem
import com.example.liersbarofflinemode.ui.theme.mainActivity.ui.Bases.Screens.MainActivity.Composable.Button

@Composable
fun ActionsInMainActivity(modifier: Modifier, navController: NavHostController?=null) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier
        .fillMaxWidth()
        .offset(
            y = screenHeight * .7f,
            x = screenWidth * .3f
        )) {
        Button(onClick = {
            navController?.navigate(NavigationItem.PlayersNameActivity.route)
        }, text = "multiple")
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = {navController?.navigate(NavigationItem.SinglePlayersScreen.route)}, text = "Single")
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = {}, text = "Lan")
    }


}