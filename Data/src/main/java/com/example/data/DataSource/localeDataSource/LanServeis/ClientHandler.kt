package com.example.data.DataSource.localeDataSource.LanServeis

import com.example.domain.GameEvents.Event
import com.example.domain.GameEvents.CardPlayEvent
import com.example.domain.GameEvents.JoinToGameEvent
import com.example.domain.GameEvents.LiarCallEvent
import com.example.domain.GameEvents.PlayerShotEvent
import com.example.domain.GameEvents.RoomStateEvent
import com.example.domain.GameEvents.StartRoundEvent
import com.example.domain.GameEvents.WarningEvent
import org.java_websocket.WebSocket

class ClientHandler(
    val client: GameWebSocketClient,
    val onRoomUpdate : (RoomStateEvent)->Unit,
    val onPlayCard : (CardPlayEvent)-> Unit,
    val onLiarCall : (LiarCallEvent)-> Unit,
    val onWarning  :  (WarningEvent)-> Unit
) {
    fun handle(conn : WebSocket ,event: Event ){
        when(event){

            is RoomStateEvent -> {
                onRoomUpdate(event)
            }
            is CardPlayEvent -> {
                onPlayCard(event)
            }

            is JoinToGameEvent -> {
            }
            is LiarCallEvent -> {}
            is PlayerShotEvent -> {}
            is WarningEvent->{
                onWarning(event)
            }
            else -> {}
        }
    }
}