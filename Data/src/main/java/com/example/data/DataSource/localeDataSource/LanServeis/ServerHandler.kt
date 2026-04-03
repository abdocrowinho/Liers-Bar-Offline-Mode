package com.example.data.DataSource.localeDataSource.LanServeis

import com.example.data.DataSource.Utltity.FunctionHelper
import com.example.domain.Entitys.LanUserEntity
import com.example.domain.GameEvents.BotVoteEvent
import com.example.domain.GameEvents.BotVoteStateEvent
import com.example.domain.GameEvents.CardPlayEvent
import com.example.domain.GameEvents.Event
import com.example.domain.GameEvents.GameOverEvent
import com.example.domain.GameEvents.JoinToGameEvent
import com.example.domain.GameEvents.LiarCallEvent
import com.example.domain.GameEvents.LiarCallResult
import com.example.domain.GameEvents.PlayAgainEvent
import com.example.domain.GameEvents.PlayerShotEvent
import com.example.domain.GameEvents.ReconnectEvent
import com.example.domain.GameEvents.RoomStateEvent
import com.example.domain.GameEvents.StartRoundEvent
import com.example.domain.GameEvents.WarningEvent
import com.example.domain.Utlites.getMyIpAddress
import org.java_websocket.WebSocket

class ServerHandler(
    private val server: GameWebSocketServer,
    private val broadcast: (Event) -> Unit,
    private val dealCards: () -> Unit,
) {

    fun handle(conn: WebSocket?, event: Event) {
        when (event) {

            is JoinToGameEvent -> {
                if (server.players.value.size >= 4) return

                server.idCounter.value += 1
                val newPlayer = event.player.copy(id = server.idCounter.value)

                server.players.value = server.players.value.toMutableMap().apply {
                    put(conn, newPlayer)
                }

                val totalPlayers = server.players.value.size

                if (totalPlayers >= 4) {
                    server.botVoteActive.value = false
                    server.startNewRound()
                } else {
                    broadcast(
                        RoomStateEvent(
                            playersInRoom = server.players.value.values.toMutableList(),
                            baseTable = server.baseTable.value,
                            tablesCards = server.tablesCards.value,
                            round = server.roundCounter.value,
                            currentTurnId = server.currentTurnId.value
                        )
                    )

                    server.botVoteActive.value = true
                    server.botVoteYesCount.value = 0
                    server.broadcastBotVoteDialog(totalPlayers)
                }
            }

            is CardPlayEvent -> {
                val oldPlayer = server.players.value[conn] ?: return
                val remainingCards = oldPlayer.cards.filterNot { it in event.card }
                val updatedPlayer = oldPlayer.copy(cards = remainingCards.toMutableList())

                server.players.value = server.players.value.toMutableMap().apply {
                    put(conn, updatedPlayer)
                }

                server.tablesCards.value = (server.tablesCards.value + event.card).toMutableList()
                server.cardsUnderTest.value = (server.cardsUnderTest.value + event.card).toMutableList()

                server.lastPlayedCards.value = event.card.toMutableList()
                server.lastThrowerId.value = event.playerId

                val nextId = FunctionHelper.nextPlayer(server.players, event.playerId)
                server.currentTurnId.value = nextId

                val nextPlayer = server.players.value.values.find { it?.id == nextId }
                if (nextPlayer?.isBot == true) {
                    server.scheduleBotTurn(nextPlayer)
                }

                broadcast(
                    CardPlayEvent(
                        playerId = event.playerId,
                        card = event.card,
                        index = event.card.size,
                        nextPlayer = nextId
                    )
                )

                broadcast(
                    RoomStateEvent(
                        playersInRoom = server.players.value.values.toMutableList(),
                        round = server.roundCounter.value,
                        tablesCards = server.tablesCards.value,
                        baseTable = server.baseTable.value,
                        currentTurnId = nextId
                    )
                )
            }

            is LiarCallEvent -> {
                broadcast(LiarCallEvent(callerId = event.callerId))

                val hasWrongCard = server.lastPlayedCards.value
                    .any { it.rank != server.baseTable.value?.rank && it.rank != com.example.domain.Entitys.Rank.Joker }

                val throwerId = server.lastThrowerId.value
                val loserId = if (hasWrongCard) throwerId else event.callerId

                val loser = server.players.value.values.find { it?.id == loserId }
                val loserConn = server.players.value.entries.find { it.value?.id == loserId }?.key
                val isRealBullet = loser?.remainingBullets == loser?.numOfShot

                val updatedLoser: LanUserEntity? = if (isRealBullet) {
                    loser?.copy(remainingBullets = 0, isAlive = false)
                } else {
                    loser?.copy(remainingBullets = (loser.remainingBullets - 1).coerceAtLeast(0))
                }

                server.players.value = server.players.value.toMutableMap().apply {
                    put(loserConn, updatedLoser)
                }

                broadcast(
                    LiarCallResult(
                        loserId = loserId,
                        isRealBullet = isRealBullet,
                        cardsRank = server.lastPlayedCards.value
                    )
                )

                server.cardsUnderTest.value = mutableListOf()
                server.lastPlayedCards.value = mutableListOf()
                server.lastThrowerId.value = 0
                server.roundStarted.value = false

                val alivePlayers = server.players.value.values.filter { it?.isAlive == true }
                if (alivePlayers.size == 1) {
                    val winner = alivePlayers.first()
                    broadcast(
                        GameOverEvent(
                            winnerId = winner?.id ?: 0,
                            winnerName = winner?.name ?: ""
                        )
                    )
                }
            }

            is ReconnectEvent -> {
                handle(conn, JoinToGameEvent(
                    player = LanUserEntity(
                        name = event.playerName,
                        numOfShot = (1..6).random(),
                        remainingBullets = 6,
                        isAlive = true,
                        image = "https://robohash.org/${event.playerName}?set=set5",
                        id = 0,
                        ipAddress = conn?.remoteSocketAddress?.address?.hostAddress ?: "",
                        cards = mutableListOf(),
                        isHost = false,
                        deviceId = event.deviceId
                    ),
                    deviceId = event.deviceId
                ))
            }

            is BotVoteEvent -> {
                if (!server.botVoteActive.value) return

                if (!event.vote) {
                    server.botVoteYesCount.value = 0
                    server.botVoteActive.value = false
                    broadcast(BotVoteStateEvent(
                        yesCount = 0,
                        totalPlayers = server.players.value.size,
                        rejected = true
                    ))
                    return
                }

                server.botVoteYesCount.value += 1

                val totalHumans = server.players.value.values
                    .filterNotNull()
                    .count { !it.isBot }

                broadcast(BotVoteStateEvent(
                    yesCount = server.botVoteYesCount.value,
                    totalPlayers = totalHumans,
                    accepted = server.botVoteYesCount.value >= totalHumans
                ))

                if (server.botVoteYesCount.value >= totalHumans) {
                    server.botVoteActive.value = false
                    server.botVoteYesCount.value = 0
                    server.addBotsToFill()
                }
            }
            is WarningEvent -> broadcast(WarningEvent)
            is PlayAgainEvent -> { /* host handles directly */ }
            is StartRoundEvent -> { /* host handles directly */ }
            is RoomStateEvent -> {}
            is PlayerShotEvent -> {}
            else -> {}
        }
    }
}