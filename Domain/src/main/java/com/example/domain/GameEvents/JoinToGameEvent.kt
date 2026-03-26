package com.example.domain.GameEvents

import com.example.domain.Entitys.LanUserEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("JoinedGameEvent")
data class JoinToGameEvent(
    val player: LanUserEntity,
    val deviceId: String = ""
):Event()