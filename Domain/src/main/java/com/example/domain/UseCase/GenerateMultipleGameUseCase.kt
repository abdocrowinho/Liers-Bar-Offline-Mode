package com.example.domain.UseCase

import com.example.domain.Entitys.UserEntity
import com.example.domain.Repo.GamePlayRepo
import javax.inject.Inject

class GenerateMultipleGameUseCase @Inject constructor(
    private val gamePLayRepo : GamePlayRepo
)  {
    fun invoke(playersNames : List<String>):List<UserEntity>{
       return gamePLayRepo.generateMultipleGame(playersNames)
    }

}