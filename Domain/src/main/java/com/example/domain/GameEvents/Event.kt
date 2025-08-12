package com.example.domain.GameEvents

import com.example.domain.Entitys.Card
import com.example.domain.Entitys.LanUserEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Event")
sealed class Event {}