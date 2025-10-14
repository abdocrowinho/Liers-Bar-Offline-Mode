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
    val tableBase : Card?
):Event()