package com.example.domain.GameEvents

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("StartRound")
data object StartRoundEvent :Event()