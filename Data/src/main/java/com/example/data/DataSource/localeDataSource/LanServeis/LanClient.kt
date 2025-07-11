package com.example.data.DataSource.localeDataSource.LanServeis

import com.example.domain.Entitys.LanUserEntity
import com.example.domain.GameEvents.Event
import kotlinx.coroutines.flow.MutableStateFlow
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

     val playersInRoom = MutableStateFlow<List<LanUserEntity?>?>(
         emptyList()
     )

    override fun onOpen(handshakedata: ServerHandshake?) {
        println("✅ Connected to server")
        onConnectedSuccess()
    }

    override fun onMessage(message: String?) {
        println("📩 Message from server: $message")
        if (message != null) {
            try {
                when( val event =  Json.decodeFromString(Event.serializer(),message)){
                    is Event.CardPlayEvent ->{}
                    is Event.GameEvent -> {



                    }
                    is Event.JoinedGameEvent -> {}
                    is Event.LiarCallEvent -> {}
                    is Event.PlayerShotEvent ->{}
                    is Event.RoomStateEvent -> {
                        val players = event.playersInRoom.mapNotNull{
                            if (it is Event.JoinedGameEvent)it.player else null
                        }
                        playersInRoom.value =players
                        if (players.size==1&&!hasStartRound){
                            hasStartRound = true
                            sendGameEvent(Event.StartRound)
                        }


                    }

                    Event.StartRound -> {
                            val json = Json.encodeToString(Event.serializer(),Event.StartRound)
                        send(json)



                    }
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
        println("❗ Error: ${ex?.message}")
        onConnectedError(ex?.message.toString())
    }

    fun sendGameEvent(event: Event) {
        val json = Json.encodeToString(Event.serializer(),event)
        send(json)
    }
}
