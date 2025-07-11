 package com.example.data.DataSource.localeDataSource.LanServeis

import com.example.domain.Utlites.Constant
import com.example.domain.Entitys.Card
import com.example.domain.Entitys.LanUserEntity
import com.example.domain.Entitys.Rank
import com.example.domain.GameEvents.Event
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import java.net.InetSocketAddress

@Suppress("NAME_SHADOWING")
class GameWebSocketServer(port: Int) : WebSocketServer(InetSocketAddress(port)) {

    private val clients = mutableSetOf<WebSocket>()
    private val players = mutableMapOf<WebSocket, LanUserEntity>()
    private var idCounter = 0
    var roundStarted = false

     private var listOfCard= mutableListOf(Card(Rank.ACE,0,"",0))
    init {
       listOfCard = Constant.listOfCard.toMutableList()

    }

    override fun onOpen(conn: WebSocket, handshake: ClientHandshake?) {
        println("✅ New connection: ${conn.remoteSocketAddress}")
        clients.add(conn)
    }

    override fun onClose(conn: WebSocket, code: Int, reason: String?, remote: Boolean) {
        println("❌ Disconnected: ${conn.remoteSocketAddress}")
        clients.remove(conn)
        players.remove(conn)
    }

    override fun onMessage(conn: WebSocket, message: String?) {
        if (message == null) return

        println("📩 Message from ${conn.remoteSocketAddress}: $message")

        try {
            when (
                val event = Json.decodeFromString(Event.serializer(),message)
            ) {
                is Event.CardPlayEvent -> {}
                is Event.GameEvent -> {}
                is Event.JoinedGameEvent -> {
                    if(players.size==4) return
                    players[conn] = event.player.copy(id = idCounter++)
                    println("✅ Player joined: ${event.player.numOfShot}")
                    val updatedState = Event.RoomStateEvent(
                        players.entries.mapIndexed { _, mutableEntry ->
                            Event.JoinedGameEvent( player = mutableEntry.value)

                        }

                    )
                    val json = Json.encodeToString(Event.serializer(), updatedState)


                    clients.forEach { it.send(json) }
                }
                is Event.LiarCallEvent -> {}
                is Event.PlayerShotEvent -> {}
                is Event.RoomStateEvent -> {}
                is Event.StartRound ->{
                    if (roundStarted)return
                    roundStarted = true
                    if (players.isNotEmpty()) {
                        listOfCard.shuffle()
// update player with take new cards
                        players.keys.forEach { conn ->
                            val player = players[conn]
                            val hand = listOfCard.take(5)
                            if (player!=null){
                                listOfCard.removeAll(hand)
                                val updatePlayer = player.copy(cards = hand)
                                players[conn] = updatePlayer
                            }

                        }
                        //send to clients new event
                    
                        val allPlayers = players.values.map {
                            Event.JoinedGameEvent(it)
                        }
                        val event = Event.RoomStateEvent(allPlayers)

                        val json = Json.encodeToString(Event.serializer(), event)
                        players.keys.forEach{
                                conn->
                            conn.send(json)
                        }
                    }
                }
            }

        } catch (e: Exception) {
            println("❗ Error handling message: ${e.message}")
        }
    }

    override fun onError(conn: WebSocket?, ex: Exception?) {
        println("❗ WebSocket error: ${ex?.message}")
    }

    override fun onStart() {

        println("🚀 WebSocket server started on port $port")
    }
}