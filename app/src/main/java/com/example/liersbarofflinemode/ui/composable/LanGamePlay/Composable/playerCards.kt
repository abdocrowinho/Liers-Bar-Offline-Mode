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
    activePlayer: LanUserEntity
) {
    val validCards = activePlayer.cards.filter { it.imageCard != 0 }

    LazyRow(modifier = modifier) {
        items(validCards, key = { it.id }) { card ->
            OurCard(card = card, viewModel = lanGamePlayViewModel)
        }
    }
}