package com.example.domain.Entitys

import kotlinx.serialization.Serializable

@Serializable
data class LanUserEntity(
    val id : Int,
    val ipAddress : String,
    val name: String ,
    val image: String,
    val isAlive: Boolean ,
    val numOfShot: Int ,
    val remainingBullets: Int,
    val  cards :List<Card>
)
