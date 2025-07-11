package com.example.domain.GameEvents.common

import com.example.domain.Entitys.LanUserEntity
import com.example.domain.GameEvents.Event
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("JoinedGameEvent")
data class JoinToGameEvent(
    val player: LanUserEntity
)