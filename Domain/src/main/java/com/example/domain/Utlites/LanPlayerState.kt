package com.example.domain.Utlites

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object MangerLanPlayerState {
    val state = MutableStateFlow(LanPlayerState.ThereISNoState)
    var playerName: String = ""
    var deviceId: String = ""

    fun setState(newState: LanPlayerState, name: String = "", id: String = "") {
        state.value = newState
        playerName = name
        deviceId = id
    }
}

enum class LanPlayerState {
    Host,
    Client,
    ThereISNoState
}