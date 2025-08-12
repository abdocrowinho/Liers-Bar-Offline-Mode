package com.example.data.DataSource.localeDataSource.LanServeis

import android.util.Log
import com.example.domain.Entitys.Card
import com.example.domain.Entitys.LanUserEntity
import com.example.domain.GameEvents.Event
import com.example.domain.GameEvents.RoomStateEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI
import kotlinx.serialization.json.*

class GameWebSocketClient(
    serverUri: URI,
    val onConnectedSuccess : ()-> Unit,
    val onConnectedError : (String) -> Unit
) : WebSocketClient(serverUri) {
    private var hasStartRound = false

    private var _messageEvent = MutableSharedFlow<Event>()
    val messageEvent : SharedFlow<Event> get() = _messageEvent


     val playersInRoom = MutableStateFlow<MutableList<LanUserEntity?>?>(mutableListOf())
     private var client : ClientHandler ?=null
    private var tablesCards : MutableList<Card>?= mutableListOf()
    private lateinit var baseTable : Card
    private var roundCounter = 0

    init {

    client = ClientHandler(client = this,
        onRoomUpdate = { event->

            playersInRoom.value =  event.playersInRoom.toMutableList()
            tablesCards = event.tablesCards
            baseTable = event.tableBase!!
            roundCounter = event.round ?:0
            RoomStateEvent(event.playersInRoom,0,tablesCards,baseTable)
                .let { _messageEvent.tryEmit(it) }

       } ,

    onPlayCard = { cardPlayEvent ->

_messageEvent.tryEmit(cardPlayEvent)
     }) {}

    }

    override fun onOpen(handshakedata: ServerHandshake?) {
        println("✅ Connected to server")
        onConnectedSuccess()
    }

    override fun onMessage(message: String?) {
        println("📩 Message from server: $message")
        if (message != null) {

            val event =  Json.decodeFromString(Event.serializer(),message)

            try {
                client?.handle(conn = connection,event)

                 CoroutineScope(Dispatchers.IO).launch {
                 _messageEvent.emit(event)

                 }

            } catch (e: Exception) {
                println("❌ Failed to parse event: ${e.message}")
            }
        }
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
        val json = Json.encodeToString(Event.serializer(),event)
        Log.d("test players in clinet", json)
        send(json)
    }
}
