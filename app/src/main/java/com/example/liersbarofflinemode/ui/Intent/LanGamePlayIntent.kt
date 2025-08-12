package com.example.liersbarofflinemode.ui.Intent

import com.example.domain.Entitys.Card
import com.example.domain.GameEvents.Event

sealed class LanGamePlayIntent {
    data class AddReadyCard(val card: Card) : LanGamePlayIntent()
    data class EmitCardsAreReady(val readyCard: List<Card>):LanGamePlayIntent()
    data class RemoveReadyCard(val card:Card):LanGamePlayIntent()

}