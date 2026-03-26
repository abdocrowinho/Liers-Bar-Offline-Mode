package com.example.domain.GameEvents

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("BotVoteEvent")
data class BotVoteEvent(
    val playerId: Int,
    val vote: Boolean
) : Event()

@Serializable
@SerialName("BotVoteStateEvent")
data class BotVoteStateEvent(
    val yesCount: Int,
    val totalPlayers: Int,
    val accepted: Boolean = false,
    val rejected: Boolean = false,
    val showDialog: Boolean = false
) : Event()