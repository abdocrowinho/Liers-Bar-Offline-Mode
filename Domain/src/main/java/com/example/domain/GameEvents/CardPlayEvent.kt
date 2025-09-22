package com.example.domain.GameEvents

import com.example.domain.Entitys.Card
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
    @Serializable
    @SerialName("CardPlayEvent")
    data class CardPlayEvent(
        val playerId : Int,
        val card:List<Card> ,
        val index : Int ,
        val nextPlayer : Int
    ):Event()
