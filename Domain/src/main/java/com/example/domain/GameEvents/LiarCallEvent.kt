package com.example.domain.GameEvents

import com.example.domain.Entitys.Card
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("LiarCallEvent")
data class LiarCallEvent(
    val  cardPlayEvent: CardPlayEvent,
    val cardTable : Card,
    val callerId: String,
    val accusedId: String,
    val loserId: String
):Event()