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

class ServerHandler(
    private val server:  GameWebSocketServer,
    private val broadcast: (Event) -> Unit,
    private val dealCards: () -> Unit,
) {

    fun handle(conn: WebSocket, event: Event) {
        when (event) {

            is JoinToGameEvent -> {
           server.idCounter.value = server.idCounter.value.apply{
               server.idCounter.value.plus(1)
           }
                if (server.players.value .size != 4) {
                    val playerEvent = event.player
                    val playerWithUId = playerEvent.copy(id = server.idCounter.value)

               server.players.value= server.players.value.toMutableMap().apply {
                       put(conn,playerWithUId)
                    }
                    broadcast(
                        RoomStateEvent(
                            server.players.value.values.toMutableList(),
                            tableBase = null,
                            tablesCards = server.tablesCards.value,
                            round = server.roundCounter.value
                        )
                    )
                }
            }

            is CardPlayEvent -> {
                val oldPlayer = server.players.value[conn]
                val oldCards = server.players.value[conn]?.cards

                val remainingCards = oldCards?.filterNot { it in event.card }

                val updatedPlayer = remainingCards?.toMutableList()
                    ?.let { oldPlayer?.copy(cards = it) }

                updatedPlayer?.let { _updatedPlayer->
             server.players.value=  server.players.value.toMutableMap().apply {
                        put(conn,_updatedPlayer)
                    }
                }
              server.tablesCards.value =
                  server.tablesCards.value.apply {
                      addAll(event.card)
                  }

                broadcast(CardPlayEvent(event.playerId, event.card, event.index,event.nextPlayer))

                broadcast(
                    RoomStateEvent(
                     playersInRoom = server.players.value.values.toMutableList(),
                        round = server.roundCounter.value,
                        tablesCards = server.tablesCards.value,
                        tableBase = server.baseTable.value
                    ),
                )
            }

            is LiarCallEvent -> {}
            is PlayerShotEvent -> {}
            is StartRoundEvent -> {
                    dealCards()
                    broadcast(RoomStateEvent(server.players.value.values.toMutableList(),
                        server.roundCounter.value,server.tablesCards.value,server.baseTable.value))
                }


            is RoomStateEvent -> {}
            is WarningEvent->{
                broadcast(WarningEvent)
            }
            else -> {}
        }
    }
}