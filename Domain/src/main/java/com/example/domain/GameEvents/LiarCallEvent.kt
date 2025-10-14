package com.example.domain.GameEvents

import com.example.domain.Entitys.Card
import com.example.domain.Entitys.Rank
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("LiarCallEvent")
data class LiarCallEvent(
    val callerId: Int,

):Event()