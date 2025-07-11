package com.example.domain.Repo

import com.example.domain.Entitys.UserEntity
import kotlinx.coroutines.flow.MutableStateFlow

interface SinglePlayerRepo {
    fun generateSingleGame(playerName : String)
    fun getPlayerState(): MutableStateFlow<UserEntity>

}