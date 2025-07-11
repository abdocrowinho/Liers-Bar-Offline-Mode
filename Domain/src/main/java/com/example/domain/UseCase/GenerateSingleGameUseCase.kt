package com.example.domain.UseCase

import com.example.domain.Repo.SinglePlayerRepo
import javax.inject.Inject

class GenerateSingleGameUseCase @Inject constructor(
    private val singlePlayerRepo : SinglePlayerRepo
) {
    fun invoke(pLayerName : String){
        singlePlayerRepo.generateSingleGame(pLayerName)
    }
}