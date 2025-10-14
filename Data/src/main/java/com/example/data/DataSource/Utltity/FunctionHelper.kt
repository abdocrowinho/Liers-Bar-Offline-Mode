package com.example.data.DataSource.Utltity

import com.example.domain.Entitys.LanUserEntity
import kotlinx.coroutines.flow.MutableStateFlow
import org.java_websocket.WebSocket

object FunctionHelper {
     fun afterPlayer(players: MutableStateFlow<MutableMap<WebSocket?, LanUserEntity?>>, id: Int): Int {
        val handleId = if (id == 1) 4 else {
            id.minus(1)
        }
        val player = players.value.values.find { it?.id == handleId }
        return if (player?.isAlive != false) {
            handleId
        } else {
            afterPlayer(id = handleId, players = players)
        }
    }
}