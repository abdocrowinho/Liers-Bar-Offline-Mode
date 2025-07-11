package com.example.liersbarofflinemode.ui.composable.EnterSinglePlayerScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.liersbarofflinemode.R
import com.example.liersbarofflinemode.ui.Intent.EnterSinglePlayerIntent
import com.example.liersbarofflinemode.ui.Utltiy.CustomOutLineBorder
import com.example.liersbarofflinemode.ui.Utltiy.GetHeightConf
import com.example.liersbarofflinemode.ui.Utltiy.GetWidthConf
import com.example.liersbarofflinemode.ui.ViewModels.EnterSingleUserViewModel
import com.example.liersbarofflinemode.ui.composable.EnterPlayersNameScreen.Composable.ButtonsRow
import com.example.liersbarofflinemode.ui.theme.mainActivity.ui.Bases.NavigationItem
import com.example.liersbarofflinemode.ui.theme.red_orange
import com.example.liersbarofflinemode.ui.theme.warm_peach

@Composable
fun EnterSinglePlayerScreen(

    viewModel: EnterSingleUserViewModel,
    navController: NavHostController
) {
    val state by viewModel.enterState.collectAsState()
    var user by remember { mutableStateOf("") }

    var userNameError1 by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painterResource(id = R.drawable.multiple_players_screen),
                contentScale = ContentScale.Crop
            )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .offset(x = GetWidthConf() * .025f)
        ) {
            Spacer(modifier = Modifier.height(GetHeightConf() * .3f))
            Text(
                text = "enter your names",
                color = red_orange,
                fontWeight = FontWeight(weight = 1000)
            )
            Spacer(modifier = Modifier.height(GetHeightConf() * .12f))
         CustomOutLineBorder(
             width = GetWidthConf() * 0.15f,
             radius = 16.dp,
             color = warm_peach ,
             text =user ,
             onTextChange = {newText -> user = newText},
             isError = userNameError1 != null
             , supportingText = { Text(text = userNameError1?:"", color = Color.Red) }
         )
            ButtonsRow(Modifier, action1 = {
                viewModel.handleIntent(
                    EnterSinglePlayerIntent.DoneButton(userName = user)
                )
            }, action2 = {})

            Spacer(modifier = Modifier.fillMaxHeight())

        }
    }
    LaunchedEffect(key1 = state) {
        if (state.player!=null && state.error == null) {
            navController.navigate(NavigationItem.SingleGamePlay.route)
        } else {
            val error = state.error
            userNameError1 = error

        }
    }


}
