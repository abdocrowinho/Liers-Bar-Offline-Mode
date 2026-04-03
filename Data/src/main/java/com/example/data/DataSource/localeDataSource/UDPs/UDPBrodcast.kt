package com.example.data.DataSource.localeDataSource.UDPs

import com.example.data.DataSource.localeDataSource.LanServeis.WebSocketServerManger
import com.example.domain.Entitys.RoomEntity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

object UDPBroadcaster {
    private const val UDP_PORT = 53248
    private val broadcastAddress = InetAddress.getByName("255.255.255.255")

    private var broadcastThread: Thread? = null
    private var isRunning = false

    fun startBroadcasting(room: RoomEntity) {
        isRunning = true
        broadcastThread = Thread {
            val socket = DatagramSocket()
            socket.broadcast = true

            while (isRunning) {
                val server = WebSocketServerManger.getServer()
                val livePlayers = server?.players?.value?.values
                    ?.filterNotNull()
                    ?: emptyList()

                val liveRoom = room.copy(list = livePlayers)

                val json = Json.encodeToString(liveRoom)
                val data = json.toByteArray()
                val packet = DatagramPacket(data, data.size, broadcastAddress, UDP_PORT)

                socket.send(packet)
                println("📢 Broadcasting room: $json")
                Thread.sleep(2000)
            }
            socket.close()
        }.apply { isDaemon = true }.also { it.start() }
    }

    fun stopBroadcasting() {
        isRunning = false
        broadcastThread?.interrupt()
        broadcastThread = null
    }
}