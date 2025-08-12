 package com.example.data.DataSource.localeDataSource.LanServeis

import com.example.domain.Utlites.Constant
import com.example.domain.Entitys.Card
import com.example.domain.Entitys.LanUserEntity
import com.example.domain.Entitys.Rank
import com.example.domain.GameEvents.Event
import com.example.domain.GameEvents.JoinToGameEvent
import com.example.domain.GameEvents.RoomStateEvent
import com.example.domain.Utlites.Constant.listOfCard
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
    lateinit var tableBase: Card
    private var  listOfCards = mutableListOf(Card(rank = Rank.ACE, imageCard = 0,"",1))
    private var serverHandler : ServerHandler?=null


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
    }


    private fun broadcastEvent(event: Event, except: WebSocket? = null) {
        val json = Json.encodeToString(Event.serializer(), event)
        clients.forEach { client ->
            if (client != except) {
                client.send(json)
            }
        }
    }


    private fun dealCards() {
        if (roundStarted) return
        roundStarted = true
        if (players.isNotEmpty()) {
           this.listOfCards.shuffled()
// update player with take new cards
            players.keys.forEach { conn ->
                val player = players[conn]
                val hand = listOfCard.take(5)
                if (player != null) {
                    listOfCard.toMutableList().removeAll(hand)
                    val updatePlayer = player.copy(cards = hand)
                    players[conn] = updatePlayer
                }

            }

            //send to clients new event

            val allPlayers = players.values.map {
                JoinToGameEvent(it)
            }
            val event = RoomStateEvent(allPlayers.map {
                it.player
            }  , tablesCards = tablesCards , tableBase = tableBase, round = roundCounter)

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

        println("🚀 WebSocket server started on port $port")
    }
}