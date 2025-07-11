package com.example.domain.GameEvents.eventsServer

import com.example.domain.GameEvents.Event
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("RoomStateEvent")
data class RoomStateEvent(
    val playersInRoom:  List<Event>
)