package com.example.liersbarofflinemode.ui.composable.EnterPlayersNameScreen.Composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.liersbarofflinemode.ui.Utltiy.GetWidthConf
import com.example.liersbarofflinemode.ui.theme.mainActivity.ui.Bases.Screens.MainActivity.Composable.CustomWoodButton


@Composable
fun ButtonsRow(
    modifier: Modifier,
    action1 : ()-> Unit,
    action2 : ()-> Unit,


    ) {
    Row(
        horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.Top
    ) {
        CustomWoodButton(
            onClick = action1, text = "Start", height = .08f, width = .12f
        )
        Spacer(modifier = modifier.width(GetWidthConf() * .41f))
        CustomWoodButton(onClick =action2, text = "Back", height = .08f, width = .12f)
        Spacer(modifier = modifier.width(GetWidthConf() * .01f))
    }
}
