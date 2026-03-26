package com.example.domain.GameEvents

import com.example.domain.Entitys.Card
import com.example.domain.Entitys.LanUserEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("RoomStateEvent")
data class RoomStateEvent(
    val playersInRoom:  List<LanUserEntity?>,
    val round : Int ?=0,
    val tablesCards:MutableList<Card>?,
     var baseTable: Card? = null,
    val currentTurnId: Int = 0  // add this

):Event()