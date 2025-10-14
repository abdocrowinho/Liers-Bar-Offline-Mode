package com.example.domain.GameEvents

import com.example.domain.Entitys.Card
import com.example.domain.Entitys.Rank
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("LiarCallResult")
data class LiarCallResult(
    val loserId : Int ,
    val isRealBullet : Boolean,
    val cardsRank : List<Card>
) : Event()