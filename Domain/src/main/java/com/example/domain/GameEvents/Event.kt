package com.example.domain.GameEvents

import com.example.domain.Entitys.Card
import com.example.domain.Entitys.LanUserEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Serializable
@SerialName("Event")
sealed class Event {

    @Serializable
    @SerialName("CardPlayEvent")
    data class CardPlayEvent(
        val playerId : String,
        val card:List<Card> ,
        val index : Int
    ):Event()

    @Serializable
    @SerialName("GameEvent")
    data class GameEvent(
        val type:String ,
        val data : String
    ):Event()

    @Serializable
    @SerialName("JoinedGameEvent")
    data class JoinedGameEvent(
        val player: LanUserEntity
    ):Event()



    @Serializable
    @SerialName("LiarCallEvent")
    data class LiarCallEvent(
        val  cardPlayEvent:CardPlayEvent,
        val cardTable : Card,
        val callerId: String,
        val accusedId: String,
        val loserId: String
    ):Event()


    @Serializable
    @SerialName("PlayerShotEvent")
    data class PlayerShotEvent(
        val playerReady : Boolean ,
        val playerId : String ,
        val isPlayerShot : Boolean ,
        val isBullet : Boolean
    ):Event()

    @Serializable
    @SerialName("StartRound")
    data object StartRound : Event()

    @Serializable
    @SerialName("RoomStateEvent")
    data class RoomStateEvent(
        val playersInRoom:  List<Event>
    ) :Event()
}