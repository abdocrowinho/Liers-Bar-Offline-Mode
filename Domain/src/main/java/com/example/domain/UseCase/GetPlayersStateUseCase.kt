package com.example.domain.UseCase

import com.example.domain.Entitys.UserEntity
import com.example.domain.Repo.GamePlayRepo
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class GetPlayersStateUseCase @Inject constructor(
    private val gamePlayRepo: GamePlayRepo
) {
    fun getGamePlayRepo():Map<Int,MutableStateFlow<UserEntity?>> {
        return gamePlayRepo.getPlayersState()
    }
}