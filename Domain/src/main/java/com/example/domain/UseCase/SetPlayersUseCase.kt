package com.example.domain.UseCase

import com.example.domain.Entitys.UserEntity
import com.example.domain.Repo.GamePlayRepo
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class SetPlayersUseCase @Inject constructor(
    private val gamePlayRepo: GamePlayRepo
) {
    fun invoke(playersList : List<UserEntity>) {
         gamePlayRepo.setPlayers(playersList)
    }
}