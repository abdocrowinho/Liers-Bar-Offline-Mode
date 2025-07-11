package com.example.domain.UseCase

import com.example.domain.Entitys.UserEntity
import com.example.domain.Repo.SinglePlayerRepo
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class GetSinglePlayerUseCase @Inject constructor(
    private val singlePlayerRepo: SinglePlayerRepo
) {
    fun invoke(): MutableStateFlow<UserEntity>{
        return singlePlayerRepo.getPlayerState()
    }
}