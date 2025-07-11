package com.example.domain.GameEvents.eventsServer

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("PlayerShotEvent")
data class PlayerShotEvent(
    val playerReady : Boolean ,
    val playerId : String ,
    val isPlayerShot : Boolean ,
    val isBullet : Boolean
)