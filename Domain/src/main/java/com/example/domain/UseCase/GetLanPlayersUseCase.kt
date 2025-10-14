package com.example.domain.UseCase

import com.example.domain.Entitys.LanUserEntity
import com.example.domain.Repo.LanGamePLay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetLanPlayersUseCase @Inject constructor(
    private val lanGamePLay: LanGamePLay
) {
   suspend fun invoke() : Flow<List<LanUserEntity?>?>{
     return lanGamePLay.getLanPlayers()
    }
}