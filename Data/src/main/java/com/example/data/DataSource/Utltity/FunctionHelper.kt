package com.example.data.DataSource.Utltity

import com.example.domain.Entitys.LanUserEntity
import kotlinx.coroutines.flow.MutableStateFlow
import org.java_websocket.WebSocket

object FunctionHelper {

    fun nextPlayer(
        players: MutableStateFlow<MutableMap<WebSocket?, LanUserEntity?>>,
        currentId: Int
    ): Int {
        val alivePlayers = players.value.values
            .filterNotNull()
            .filter { it.isAlive }
            .sortedBy { it.id }

        if (alivePlayers.isEmpty()) return currentId
        if (alivePlayers.size == 1) return alivePlayers.first().id

        val currentIndex = alivePlayers.indexOfFirst { it.id == currentId }
        if (currentIndex == -1) return alivePlayers.first().id

        val nextIndex = (currentIndex + 1) % alivePlayers.size
        return alivePlayers[nextIndex].id
    }

    fun previousPlayer(
        players: MutableStateFlow<MutableMap<WebSocket?, LanUserEntity?>>,
        currentId: Int
    ): Int {
        val alivePlayers = players.value.values
            .filterNotNull()
            .filter { it.isAlive }
            .sortedBy { it.id }

        if (alivePlayers.isEmpty()) return currentId
        if (alivePlayers.size == 1) return alivePlayers.first().id

        val currentIndex = alivePlayers.indexOfFirst { it.id == currentId }
        if (currentIndex == -1) return alivePlayers.first().id

        val prevIndex = (currentIndex - 1 + alivePlayers.size) % alivePlayers.size
        return alivePlayers[prevIndex].id
    }
}