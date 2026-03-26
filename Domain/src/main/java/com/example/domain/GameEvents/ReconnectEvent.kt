package com.example.domain.GameEvents

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("ReconnectEvent")
data class ReconnectEvent(
    val deviceId: String,
    val playerName: String
) : Event()