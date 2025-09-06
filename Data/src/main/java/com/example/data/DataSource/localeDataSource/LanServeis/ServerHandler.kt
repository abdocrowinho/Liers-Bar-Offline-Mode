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
                server.idCounter = server.idCounter.plus(1)
                if (server.players.size != 4) {
                    val playerEvent = event.player
                    val playerWithUId = playerEvent.copy(id = server.idCounter)
                    server.players[conn] = playerWithUId
                    broadcast(
                        RoomStateEvent(
                            server.players.values.toMutableList(),
                            tableBase = null,
                            tablesCards = server.tablesCards,
                            round = 1
                        )
                    )
                }

            }

            is CardPlayEvent -> {
                val oldPlayer = server.players[conn]
                val oldCards = server.players[conn]?.cards

                val remainingCards = oldCards?.filterNot { it in event.card }

                val updatedPlayer = remainingCards?.toMutableList()
                    ?.let { oldPlayer?.copy(cards = it) }
                updatedPlayer?.let {
                    server.players[conn] = it
                }
                server.tablesCards.addAll(event.card)

                broadcast(CardPlayEvent(event.playerId, event.card, event.index,event.nextPlayer))

                broadcast(
                    RoomStateEvent(
                     playersInRoom = server.players.values.toMutableList(),
                        round = 0,
                        tablesCards = server.tablesCards,
                        tableBase = server.baseTable
                    ),
                )


            }

            is LiarCallEvent -> {}
            is PlayerShotEvent -> {}
            is StartRoundEvent -> {
                    dealCards()
                    broadcast(RoomStateEvent(server.players.values.toMutableList(),
                        server.roundCounter,server.tablesCards,server.baseTable))
                }


            is RoomStateEvent -> {}
            is WarningEvent->{
                broadcast(WarningEvent)
            }
            else -> {}
        }
    }
}