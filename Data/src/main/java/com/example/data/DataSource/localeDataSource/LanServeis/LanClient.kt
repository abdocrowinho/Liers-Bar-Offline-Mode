package com.example.data.DataSource.localeDataSource.LanServeis

import android.util.Log
import com.example.data.DataSource.Utltity.JsonHelper
import com.example.domain.Entitys.Card
import com.example.domain.Entitys.LanUserEntity
import com.example.domain.GameEvents.CardPlayEvent
import com.example.domain.GameEvents.Event
import com.example.domain.GameEvents.RoomStateEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI
import kotlinx.serialization.json.*

class GameWebSocketClient(
    serverUri: URI,
    val onConnectedSuccess: () -> Unit,
    val onConnectedError: (String) -> Unit
) : WebSocketClient(serverUri) {

    private var _messageEvent = MutableSharedFlow<Event>(
        replay = 0,
        extraBufferCapacity = 20
    )
    val messageEvent: SharedFlow<Event> get() = _messageEvent

    val playersInRoom = MutableStateFlow<MutableList<LanUserEntity?>?>(mutableListOf())
    private var client: ClientHandler? = null
    private var tablesCards: MutableList<Card>? = mutableListOf()
    private var baseTable: Card? = null
    private var roundCounter = 0

    init {
        client = ClientHandler(
            client = this,
            onRoomUpdate = { event ->
                playersInRoom.value = event.playersInRoom.toMutableList()
                tablesCards = event.tablesCards
                if (event.baseTable != null) baseTable = event.baseTable
                roundCounter = event.round ?: 0
                _messageEvent.tryEmit(event)
            },
            onPlayCard = { cardPlayEvent ->
                _messageEvent.tryEmit(cardPlayEvent)
            },
            onLiarCall = { liarCallEvent ->
                _messageEvent.tryEmit(liarCallEvent)
            },
            onLiarCallResult = { liarCallResult ->
                _messageEvent.tryEmit(liarCallResult)
            },
            onWarning = { warningEvent ->
                _messageEvent.tryEmit(warningEvent)
            },
                    onGameOver = { event ->
                _messageEvent.tryEmit(event)
            },
            onPlayAgain = { event ->
                _messageEvent.tryEmit(event)
            },
            onBotVoteState = { event ->
                _messageEvent.tryEmit(event)
            }
        )
    }

    override fun onMessage(message: String?) {
        println("📩 Message from server: $message")
        if (message == null) return
        try {
            val event = JsonHelper.Json.decodeFromString(Event.serializer(), message)
            println("✅ Decoded event: ${event::class.simpleName}")
            client?.handle(conn = connection, event)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onOpen(handshakedata: ServerHandshake?) {
        println("✅ Connected to server")
        onConnectedSuccess()

    }
    override fun onClose(code: Int, reason: String?, remote: Boolean) {
        println("❌ Disconnected from server: $reason")
    }

    override fun onError(ex: Exception?) {
        ex?.printStackTrace()
        println("❗ Error: ${ex?.message}")
        onConnectedError(ex?.message.toString())
    }

    fun sendGameEvent(event: Event) {
        val json = Json.encodeToString(Event.serializer(), event)
        send(json)
    }
}
