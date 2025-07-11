package com.example.liersbarofflinemode.ui.Intent

sealed class EndGameDialogIntent {
    data class PlayAgainIntent(val onPlayAgain: () -> Unit) : EndGameDialogIntent()
    data class Back(val onBackAction: () -> Unit) : EndGameDialogIntent()

}