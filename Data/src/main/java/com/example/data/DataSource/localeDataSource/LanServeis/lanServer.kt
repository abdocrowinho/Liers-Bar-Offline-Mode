package com.example.data.DataSource.localeDataSource.LanServeis

import android.util.Log
import com.example.domain.Entitys.LanUserEntity
import com.example.domain.GameEvents.Event
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import java.net.InetSocketAddress

class GameWebSocketServer(port: Int) : WebSocketServer(InetSocketAddress(port)) {

    private val clients = mutableSetOf<WebSocket>()
    private val players = mutableMapOf<WebSocket, LanUserEntity>()

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
                    players[conn] = event.player.copy(id = players.size+1)

                    val updatedState = Event.RoomStateEvent(
                        players.entries.mapIndexed { index, mutableEntry ->
                            Event.JoinedGameEvent( player = mutableEntry.value)

                        }

                    )

                    val gameEvent = Event.GameEvent(
                        type = "room_state",
                        data = Json.encodeToString(updatedState)
                    )

                    clients.forEach { it.send(Json.encodeToString(gameEvent)) }
                }
                is Event.LiarCallEvent -> {}
                is Event.PlayerShotEvent -> {}
                is Event.RoomStateEvent -> {}
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

    private fun broadcastExcept(sender: WebSocket, event: Event.GameEvent) {
        val message = Json.encodeToString(event)
        clients.filter { it != sender }.forEach { it.send(message) }
    }
}