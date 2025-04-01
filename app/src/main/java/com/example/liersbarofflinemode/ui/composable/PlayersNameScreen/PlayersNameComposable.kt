package com.example.liersbarofflinemode.ui.theme.mainActivity.ui.Bases.Screens.PlayersName

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.example.liersbarofflinemode.R
import com.example.liersbarofflinemode.ui.theme.mainActivity.ui.Bases.Screens.MainActivity.Composable.Button

@Composable
fun MultiplePlayersScreenBody(name: String?=null, modifier: Modifier = Modifier, navHostController: NavHostController?=null) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painter = painterResource(id = R.drawable.multiple_players_screen),
                contentScale = ContentScale.FillBounds
            )
    ){
        Column(horizontalAlignment = Alignment.CenterHorizontally , modifier = modifier.) {
            Text(text = "asd")
            Row {

            }
        }

    }
}

