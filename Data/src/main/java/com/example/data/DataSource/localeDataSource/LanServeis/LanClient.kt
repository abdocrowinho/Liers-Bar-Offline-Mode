package com.example.data.DataSource.localeDataSource.LanServeis

import android.util.Log
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
    val onConnectedSuccess : ()-> Unit,
    val onConnectedError : (String) -> Unit
) : WebSocketClient(serverUri) {

    private var _messageEvent = MutableSharedFlow<Event>(1)
    val messageEvent : SharedFlow<Event> get() = _messageEvent

     val playersInRoom = MutableStateFlow<MutableList<LanUserEntity?>?>(mutableListOf())
     private var client : ClientHandler ?=null
    private var tablesCards : MutableList<Card>?= mutableListOf()
    private lateinit var baseTable : Card
    private var roundCounter = 0

    init {

    client = ClientHandler(client = this,
        onRoomUpdate = { event->

            playersInRoom.value = event.playersInRoom.toMutableList()
            tablesCards = event.tablesCards
            baseTable = event.tableBase!!
            roundCounter = event.round ?:0

            RoomStateEvent(event.playersInRoom,roundCounter,tablesCards,baseTable)
                .let { _messageEvent.tryEmit(it) }

       } ,

    onPlayCard = { cardPlayEvent ->
_messageEvent.tryEmit(cardPlayEvent)

    }, onLiarCall = {},
        onWarning = {onWarning->
            _messageEvent.tryEmit(onWarning)
        }
    )
        CoroutineScope(Dispatchers.IO).launch {
            messageEvent.collect{messageEvent->
                Log.d("message event in client",messageEvent.toString())
            }
        }
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

                 _messageEvent.tryEmit(event)

                 }

            } catch (e: Exception) {
                println("client-> ❌ Failed to parse event : ${e.message}")
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
        val json = Json.encodeToString(Event.serializer(), event)
        send(json)
    }
}
