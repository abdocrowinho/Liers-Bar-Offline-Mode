package com.example.domain.Repo

import com.example.domain.Entitys.LanUserEntity
import com.example.domain.Entitys.RoomEntity
import com.example.domain.Entitys.UserEntity
import com.example.domain.GameEvents.Event
import com.example.domain.Utlites.UiResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow

interface LanGamePLay {
    suspend fun createRoom(room : RoomEntity)
 suspend fun connectToGameServer (serverIp: String,port:String)
    suspend fun join(playerName: String)
    fun isWebSocketOpen():Flow<Boolean>
    fun getRoom(): Flow<UiResult< RoomEntity?>>
    fun sendEvent(event: Event)
    fun getLanPlayers():MutableStateFlow<MutableList<LanUserEntity?>?>
   suspend fun getMessage():SharedFlow<Event?>

}