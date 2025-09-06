package com.example.domain.UseCase

import com.example.domain.Repo.LanGamePLay
import javax.inject.Inject

class JoinGameUseCase @Inject constructor(
    val lanGamePLay: LanGamePLay
) {
   suspend fun invoke(playerName:String){
        lanGamePLay.join(playerName = playerName)
    }
}