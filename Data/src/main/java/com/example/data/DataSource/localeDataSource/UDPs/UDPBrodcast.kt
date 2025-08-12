package com.example.data.DataSource.localeDataSource.UDPs

import android.util.Log
import com.example.data.DataSource.localeDataSource.LanServeis.GameWebSocketServer
import com.example.domain.Entitys.RoomEntity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress


object UDPBroadcaster {
    private const val port = 53248
    private val broadcastAddress = InetAddress.getByName("255.255.255.255")

    fun startBroadcasting(room: RoomEntity) {
        val server = GameWebSocketServer(port)
        server.start()

        Thread {
            val socket = DatagramSocket()
            socket.broadcast = true

            val json = Json.encodeToString(room)
            val data = json.toByteArray()
            val packet = DatagramPacket(data, data.size, broadcastAddress, port)

            while (true) {
                socket.send(packet)
                println("📢 Broadcasting room: $json")
                Thread.sleep(2000)
            }
        }.start()
    }
}