package com.example.domain.UseCase

import com.example.domain.Repo.GamePlayRepo
import javax.inject.Inject

class GetGamePlayRepoUseCase @Inject constructor(
    private val gamePlayRepo: GamePlayRepo
) {
    fun getGamePlayRepo():GamePlayRepo {
        return gamePlayRepo
    }
}