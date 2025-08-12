package com.example.liersbarofflinemode.ui.Intent

sealed class StartScreenIntent {
    data object  LanButton:StartScreenIntent()
    data object  OnBoxClick:StartScreenIntent()
    data object CreateRoom : StartScreenIntent()
    data object GoingRoom : StartScreenIntent()
    data object Back:StartScreenIntent()
    data object Join:StartScreenIntent()

}