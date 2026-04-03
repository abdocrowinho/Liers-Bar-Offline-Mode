package com.example.data.DataSource.localeDataSource.LanServeis

import com.example.domain.GameEvents.BotVoteStateEvent
import com.example.domain.GameEvents.CardPlayEvent
import com.example.domain.GameEvents.Event
import com.example.domain.GameEvents.GameOverEvent
import com.example.domain.GameEvents.JoinToGameEvent
import com.example.domain.GameEvents.LiarCallEvent
import com.example.domain.GameEvents.LiarCallResult
import com.example.domain.GameEvents.PlayAgainEvent
import com.example.domain.GameEvents.PlayerShotEvent
import com.example.domain.GameEvents.RoomStateEvent
import com.example.domain.GameEvents.StartRoundEvent
import com.example.domain.GameEvents.WarningEvent
import org.java_websocket.WebSocket

class ClientHandler(
    val client: GameWebSocketClient,
    val onRoomUpdate: (RoomStateEvent) -> Unit,
    val onPlayCard: (CardPlayEvent) -> Unit,
    val onLiarCall: (LiarCallEvent) -> Unit,
    val onLiarCallResult: (LiarCallResult) -> Unit,
    val onWarning: (WarningEvent) -> Unit,
    val onGameOver: (GameOverEvent) -> Unit,
    val onPlayAgain: (PlayAgainEvent) -> Unit,
    val onBotVoteState: (BotVoteStateEvent) -> Unit,
) {
    fun handle(conn: WebSocket, event: Event) {
        when (event) {
            is RoomStateEvent -> onRoomUpdate(event)
            is CardPlayEvent -> onPlayCard(event)
            is LiarCallEvent -> onLiarCall(event)
            is LiarCallResult -> onLiarCallResult(event)
            is WarningEvent -> onWarning(event)
            is GameOverEvent -> onGameOver(event)
            is PlayAgainEvent -> onPlayAgain(event)
            is BotVoteStateEvent -> onBotVoteState(event)
            is JoinToGameEvent -> {}
            is PlayerShotEvent -> {}
            is StartRoundEvent -> {}
            else -> {}
        }
    }
}