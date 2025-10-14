package com.example.data.DataSource.localeDataSource.LanServeis

import com.example.data.DataSource.Utltity.FunctionHelper
import com.example.domain.Entitys.LanUserEntity
import com.example.domain.GameEvents.Event
import com.example.domain.GameEvents.CardPlayEvent
import com.example.domain.GameEvents.JoinToGameEvent
import com.example.domain.GameEvents.LiarCallEvent
import com.example.domain.GameEvents.LiarCallResult
import com.example.domain.GameEvents.PlayerShotEvent
import com.example.domain.GameEvents.RoomStateEvent
import com.example.domain.GameEvents.StartRoundEvent
import com.example.domain.GameEvents.WarningEvent
import kotlinx.coroutines.flow.map
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
                            tableBase = server.baseTable.value,
                            tablesCards = server.tablesCards.value,
                            round = server.roundCounter.value
                        )
                    )

                }
            }

            is CardPlayEvent -> {
                server.cardsUnderTest.value = server.cardsUnderTest.value.apply {
                 addAll(event.card.toMutableList())
                }
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
                server.cardsUnderTest.value = server.cardsUnderTest.value.apply {
                   addAll(event.card.toMutableList())
                }
            }

            is LiarCallEvent -> {

                broadcast(LiarCallEvent(callerId = event.callerId))

              val hasWrongCard = server.cardsUnderTest.value.
                  any{it.rank!=server.baseTable.value!!.rank}

         val loserId = if (hasWrongCard){
             FunctionHelper.afterPlayer(server.players,event.callerId)
           }else {
               event.callerId
           }
                val loser = server.players.value.values.find { it?.id == loserId }
                val loserConn = server.players.value.keys.find { server.players.value[it]?.id == loserId }

                val isRealBullet = (loser?.remainingBullets==loser?.numOfShot)
                val updateLoserPlayer : LanUserEntity? = if (isRealBullet ){

                    loser?.copy(remainingBullets = 0, isAlive = false)
                }else{
                    loser?.copy(remainingBullets = loser.remainingBullets.minus(1))
                }
                server.players.value = server.players.value.apply {
                    put(loserConn,updateLoserPlayer!!)
                }
                broadcast(LiarCallResult(
                    loserId = loserId,
                    isRealBullet = isRealBullet,
                    cardsRank = server.cardsUnderTest.value
                ))
                server.roundStarted.value= false
            }
            is PlayerShotEvent -> {}
            is StartRoundEvent -> {
                // something need to modify at this logic
                }


            is RoomStateEvent -> {}
            is WarningEvent->{
                broadcast(WarningEvent)
            }
            else -> {}
        }
    }
}