package com.example.domain.GameEvents.common

import com.example.domain.Entitys.Card
import com.example.domain.GameEvents.Event
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
)