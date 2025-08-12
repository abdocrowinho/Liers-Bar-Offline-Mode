package com.example.domain.Entitys

import kotlinx.serialization.Serializable


@Serializable
data class RoomEntity(
    val roomId : String ,
    val list: List<LanUserEntity>,
    val hostName : String,
    val ipHost:String
)