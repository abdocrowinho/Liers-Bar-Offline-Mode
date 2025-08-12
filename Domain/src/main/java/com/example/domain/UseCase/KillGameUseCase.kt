package com.example.domain.UseCase

import com.example.domain.Repo.GamePlayRepo
import javax.inject.Inject

class KillGameUseCase @Inject constructor(
    private  val gamePlayRepo: GamePlayRepo
) {
    fun invoke (){
        gamePlayRepo.killGame()
    }
}