package com.example.liersbarofflinemode.ui.composable.LanGamePlay.Composable

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.domain.Entitys.LanUserEntity
import com.example.liersbarofflinemode.ui.ViewModels.LanGamePlayViewModel

@Composable
fun PlayerCards(
    modifier: Modifier,
    lanGamePlayViewModel: LanGamePlayViewModel,
    activePlayer:LanUserEntity
    ){
    LazyRow( modifier =modifier) {
        items(activePlayer.cards , key = {it.id}){ card->
            OurCard( card = card , viewModel = lanGamePlayViewModel )
        }
    }

}