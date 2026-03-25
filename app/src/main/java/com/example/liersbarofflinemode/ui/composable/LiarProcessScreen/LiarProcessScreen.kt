package com.example.liersbarofflinemode.ui.composable.LiarProcessScreen

import LanPlayerAvatar
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.domain.GameEvents.StartRoundEvent
import com.example.domain.Utlites.MangerLanPlayerState
import com.example.liersbarofflinemode.R
import com.example.liersbarofflinemode.ui.Intent.EventsLanGamePlayIntent
import com.example.liersbarofflinemode.ui.Utltiy.GetHeightConf
import com.example.liersbarofflinemode.ui.Utltiy.GetWidthConf
import com.example.liersbarofflinemode.ui.ViewModels.LanGamePlayViewModel
import com.example.liersbarofflinemode.ui.composable.LanGamePlay.Composable.createTts
import com.example.liersbarofflinemode.ui.composable.LiarProcessScreen.composable.LooserProcessBody
import com.example.liersbarofflinemode.ui.theme.mainActivity.ui.Bases.NavigationItem

@Composable
fun LiarProcessScreen(navController: NavController,lanGamePlayViewModel: LanGamePlayViewModel){
    val state by lanGamePlayViewModel.uiState.collectAsState()
    val context = LocalContext.current
    val tts = remember { createTts(context) }
    LooserProcessBody(state,
        tts = tts,
        ){
        state.isRealBullet?.let {
            lanGamePlayViewModel.clearSpokenText()
            lanGamePlayViewModel.clearPlayerCallLiar()
        navController.popBackStack()
        }
    }
}
