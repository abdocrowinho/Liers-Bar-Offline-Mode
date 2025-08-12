package com.example.liersbarofflinemode.ui.Intent

sealed class GamePlayIntent {
    data class FireBullet(val index: Int) : GamePlayIntent()
}