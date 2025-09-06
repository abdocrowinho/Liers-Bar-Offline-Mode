 package com.example.data.DataSource.localeDataSource.LanServeis

import com.example.domain.Utlites.Constant
import com.example.domain.Entitys.Card
import com.example.domain.Entitys.LanUserEntity
import com.example.domain.Entitys.Rank
import com.example.domain.GameEvents.Event
import com.example.domain.GameEvents.JoinToGameEvent
import com.example.domain.GameEvents.RoomStateEvent
import com.example.domain.Utlites.Constant.listOfCard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import java.net.InetSocketAddress

@Suppress("NAME_SHADOWING")
class GameWebSocketServer(port: Int) : WebSocketServer(InetSocketAddress(port)) {

    private var roundStarted = false
    private val clients = mutableSetOf<WebSocket>()
    val players = mutableMapOf<WebSocket, LanUserEntity>()
    var idCounter = 0
    var roundCounter = 0
    var tablesCards: MutableList<Card> = mutableListOf()
     var baseTable : Card? = null
    private var  listOfCards = mutableListOf(Card(rank = Rank.ACE, imageCard = 0,"",1))
    private var serverHandler : ServerHandler?=null
    private var onStarted: (() -> Unit)? = null

    fun setOnStartedListener(listener: () -> Unit) {
        onStarted = listener
    }

    init {
        listOfCards = listOfCard.toMutableList()
        serverHandler = ServerHandler(
            server = this,
            broadcast = { event ->
                broadcastEvent(event)
            },
            dealCards = {
                dealCards()
            }
        )
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

        val event =  Json.decodeFromString(Event.serializer(),message)

        try {
            serverHandler?.handle(conn = conn,event)


        } catch (e: Exception) {
            println("server ->  ❌ Failed to parse event: ${e.message}")
        }
    }



    private fun broadcastEvent(event: Event) {
        val json = Json.encodeToString(Event.serializer(), event)
        val snapshot = clients.toList()
        snapshot.forEach { client ->
            try {
                client.send(json)
            } catch (e: Exception) {
                println("❌ Failed to send to ${client.remoteSocketAddress}: ${e.message}")
            }
        }
    }


    private fun dealCards() {
        if (roundStarted) return
        roundStarted = true
        if (players.isNotEmpty()) {
           this.listOfCards = this.listOfCards.shuffled().toMutableList()
// update player with take new cards
            players.keys.forEach { conn ->
                val player = players[conn]
                val hand = listOfCards.take(5)
                listOfCards.removeAll(hand)

                if (player != null) {
                    val updatePlayer = player.copy(cards = hand)
                    players[conn] = updatePlayer
                }

            }
            baseTable = listOfCards.random()
            //send to clients new event

            val allPlayers = players.values.map {
                JoinToGameEvent(it)
            }
            val event = RoomStateEvent(allPlayers.map {
                it.player
            }  , tablesCards = tablesCards , tableBase = baseTable, round = roundCounter)

            val json = Json.encodeToString(Event.serializer(), event)
            players.keys.forEach { conn ->
                conn.send(json)
            }
        }
    }


    override fun onError(conn: WebSocket?, ex: Exception?) {
        println("❗ WebSocket error: ${ex?.message}")
    }

    override fun onStart() {
onStarted?.invoke()
        println("🚀 WebSocket server started on port $port")
    }
}