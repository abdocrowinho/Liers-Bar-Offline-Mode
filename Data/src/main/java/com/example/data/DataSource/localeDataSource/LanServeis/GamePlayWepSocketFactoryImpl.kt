package com.example.data.DataSource.localeDataSource.LanServeis

import java.net.URI


class GamePlayWepSocketFactoryImpl : GamePlayLanWebSocketFactory {
    override fun create(
        uri: URI,
        onSuccess: () -> Unit,
        onErrorAction: (String) -> Unit
    ): GameWebSocketClient {
        return GameWebSocketClient(
            uri,
            onSuccess,
            onErrorAction
        )
    }
}