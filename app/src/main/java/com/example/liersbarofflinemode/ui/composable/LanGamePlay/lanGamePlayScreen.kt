package com.example.liersbarofflinemode.ui.composable.LanGamePlay

import LanPlayerAvatar
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.domain.Utlites.getMyIpAddress
import com.example.liersbarofflinemode.R
import com.example.liersbarofflinemode.ui.ViewModels.LanGamePlayViewModel
import com.example.liersbarofflinemode.ui.composable.LanGamePlay.Composable.CardsUnderTestAnimation
import com.example.liersbarofflinemode.ui.composable.LanGamePlay.Composable.OurCard
import com.example.liersbarofflinemode.ui.composable.LanGamePlay.Composable.TablePlayers

@Composable
fun LanGamePlayScreen(
    navController: NavHostController,
    lanGamePlayViewModel: LanGamePlayViewModel
) {
    val tableState by lanGamePlayViewModel.uiState.collectAsState()
   TablePlayers(lanGamePlayViewModel = lanGamePlayViewModel,navController)
    animateIntAsState(targetValue = 2, label = "")
    CardsUnderTestAnimation(
        tableState.cardsUnderTest
        ,navController,
        tableState.isPlayerCallingLiar,)
}