package com.example.domain.Repo

interface GamePlayLanWebSocketFactory {
    fun create(uri:String,onSuccess:()->Unit , onErrorAction: (String)->Error):WebS
}