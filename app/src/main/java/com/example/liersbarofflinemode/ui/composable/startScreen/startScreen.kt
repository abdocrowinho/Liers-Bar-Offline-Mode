package com.example.liersbarofflinemode.ui.composable.startScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.example.liersbarofflinemode.R
import com.example.liersbarofflinemode.ui.composable.startScreen.Composable.ActionsInMainActivity

@Composable
fun MyApp(modifier: Modifier, navController: NavHostController?=null){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painterResource(id = R.drawable.main_screen),
                contentScale = ContentScale.FillBounds
            )


    ) {
        ActionsInMainActivity(modifier = Modifier,navController)
    }
}
