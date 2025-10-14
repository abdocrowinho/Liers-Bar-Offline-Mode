package com.example.domain.Utlites

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object MangerLanPlayerState {
    private var _state= MutableStateFlow(LanPlayerState.ThereISNoState)
    val state = _state.asStateFlow()
    fun setState(state: LanPlayerState) {
        this._state.value = state
    }
}

enum class LanPlayerState {
    Host,
    Client,
    ThereISNoState
}