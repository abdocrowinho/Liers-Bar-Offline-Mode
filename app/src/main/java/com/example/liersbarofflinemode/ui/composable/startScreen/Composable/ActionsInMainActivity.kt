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
import com.example.liersbarofflinemode.ui.Utltiy.GetHeightConf
import com.example.liersbarofflinemode.ui.Utltiy.GetWidthConf
import com.example.liersbarofflinemode.ui.theme.mainActivity.ui.Bases.NavigationItem
import com.example.liersbarofflinemode.ui.theme.mainActivity.ui.Bases.Screens.MainActivity.Composable.Button

@Composable
fun ActionsInMainActivity(modifier: Modifier, navController: NavHostController?=null) {

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier
        .fillMaxWidth()
        .offset(
            y = GetHeightConf() * .7f,
            x = GetWidthConf() * .3f
        )) {
        Button(onClick = {
            navController?.navigate(NavigationItem.PlayersNameActivity.route)
        }, text = "multiple", height = .1f, width = .15f  )
        Spacer(modifier = Modifier.height(GetHeightConf()*.03f))
        Button(onClick = {navController?.navigate(NavigationItem.SinglePlayersScreen.route)},
            text = "Single",height = .1f, width = .15f )
        Spacer(modifier = Modifier.height(GetHeightConf()*.03f))
        Button(onClick = {}, text = "Lan",height = .1f, width = .15f )
    }


}