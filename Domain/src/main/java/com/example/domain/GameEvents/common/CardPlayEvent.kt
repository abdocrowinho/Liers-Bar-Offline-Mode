package com.example.domain.GameEvents.common

import com.example.domain.Entitys.Card
import com.example.domain.Entitys.LanUserEntity
import com.example.domain.GameEvents.Event
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


    @Serializable
    @SerialName("CardPlayEvent")
    data class CardPlayEvent(
        val playerId : String,
        val card: Card,
        val index : Int
    )
