package com.example.domain.Repo

import com.example.domain.Entitys.LanUserEntity
import com.example.domain.Entitys.RoomEntity
import com.example.domain.Entitys.UserEntity
import com.example.domain.GameEvents.Event
import com.example.domain.Utlites.UiResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface LanGamePLay {
    suspend fun createRoom(room : RoomEntity)
 suspend fun connectToGameServer (serverIp: String,
                                    onConnectedError:(String) -> Unit, onConnectedSuccess:()->Unit)
    suspend fun join(playerName: String)
    fun getRoom(): Flow<UiResult< RoomEntity?>>
    fun sendEvent(event: Event)
     fun getLanPlayers():MutableStateFlow<List<LanUserEntity?>?>

}