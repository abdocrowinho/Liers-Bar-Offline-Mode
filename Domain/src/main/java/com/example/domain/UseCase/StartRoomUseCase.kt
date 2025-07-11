package com.example.domain.UseCase

import com.example.domain.Entitys.RoomEntity
import com.example.domain.Entitys.UserEntity
import com.example.domain.Repo.LanGamePLay
import com.example.domain.Utlites.getMyIpAddress
import javax.inject.Inject

class StartRoomUseCase @Inject constructor(
     private val lanGamePLay: LanGamePLay
) {
 suspend fun invoke (player : String){
      val room = RoomEntity(roomId = (100..2000).random()
           .toString(), hostName = player , list = emptyList() , ipHost = getMyIpAddress()
      )
      lanGamePLay.createRoom(room)
 }
}