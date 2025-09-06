package com.example.domain.UseCase

import com.example.domain.Repo.LanGamePLay
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetConnectionStatusUseCase @Inject constructor(
    private val lanGamePlayRepo:LanGamePLay
) {
    fun invoke():Flow<Boolean>{
        return lanGamePlayRepo.isWebSocketOpen()
    }
}