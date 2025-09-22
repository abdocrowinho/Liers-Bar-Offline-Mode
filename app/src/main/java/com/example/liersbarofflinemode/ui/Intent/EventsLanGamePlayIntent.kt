package com.example.liersbarofflinemode.ui.Intent

import com.example.domain.Entitys.Card

sealed class EventsLanGamePlayIntent {
    data object DealCards:EventsLanGamePlayIntent()
    data class ThrowCardsButton(
                                val playerId :Int,
        ):EventsLanGamePlayIntent()
    data class CallLiarButton(val playerId: Int):EventsLanGamePlayIntent()
    data object Warning:EventsLanGamePlayIntent()

}