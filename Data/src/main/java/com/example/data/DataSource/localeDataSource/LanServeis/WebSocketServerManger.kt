package com.example.data.DataSource.localeDataSource.LanServeis

object WebSocketServerManger {

   private var server : GameWebSocketServer ?= null

    fun getServer ():GameWebSocketServer ? = server

    fun createServer(port:Int):GameWebSocketServer{
        if (server==null){
            server = GameWebSocketServer(port)
        }
        return server!!
    }
    fun stopServer() {
        server?.stop()
        server = null
    }

}