package com.example.domain.Entitys

import kotlinx.serialization.Serializable

@Serializable
data class Card(
    val rank: Rank,
    val imageCard:Int,
    val colorHex: String,
val id : Int
)
