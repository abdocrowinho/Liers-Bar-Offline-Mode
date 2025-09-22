package com.example.domain.UseCase

import com.example.domain.Repo.LanGamePLay
import javax.inject.Inject

class ConnectToRoomUseCase @Inject constructor (
    private val lanGamePlayRepo: LanGamePLay

) {
   suspend  fun invoke(ip:String,port:String){
        lanGamePlayRepo.connectToGameServer(ip,port=port)
    }
}