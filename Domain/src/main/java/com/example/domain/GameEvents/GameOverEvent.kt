package com.example.domain.GameEvents

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("GameOverEvent")
data class GameOverEvent(
    val winnerId: Int,
    val winnerName: String
) : Event()