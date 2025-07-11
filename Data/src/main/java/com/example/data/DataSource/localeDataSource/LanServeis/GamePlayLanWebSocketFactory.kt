package com.example.data.DataSource.localeDataSource.LanServeis

import java.net.URI

interface GamePlayLanWebSocketFactory {
    fun create(uri: URI, onSuccess:()->Unit, onErrorAction: (String)->Unit):GameWebSocketClient
}