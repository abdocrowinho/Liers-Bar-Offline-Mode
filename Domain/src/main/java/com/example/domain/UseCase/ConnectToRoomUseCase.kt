package com.example.domain.UseCase

import com.example.domain.Repo.LanGamePLay
import javax.inject.Inject

class ConnectToRoomUseCase @Inject constructor (
    private val lanGamePlayRepo: LanGamePLay

) {
   suspend  fun invoke(  serverIp: String,
                         port: String,
                         deviceId: String,
                         playerName: String){
        lanGamePlayRepo.connectToGameServer(serverIp = serverIp, port = port, playerName = playerName, deviceId = deviceId)
    }
}