package com.example.domain.UseCase

import com.example.domain.Repo.LanGamePLay
import javax.inject.Inject

class ConnectToRoomUseCase @Inject constructor (
    private val lanGamePlayRepo: LanGamePLay

) {
   suspend  fun invoke(ip:String, onError:(String)->Unit , onSuccess:()->Unit){
        lanGamePlayRepo.connectToGameServer(ip, onConnectedError =onError, onConnectedSuccess = onSuccess)
    }
}