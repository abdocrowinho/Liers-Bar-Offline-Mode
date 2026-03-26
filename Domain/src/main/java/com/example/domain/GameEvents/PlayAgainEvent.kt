package com.example.domain.GameEvents

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("PlayAgainEvent")
data object PlayAgainEvent : Event()