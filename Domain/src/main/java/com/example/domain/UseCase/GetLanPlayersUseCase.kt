package com.example.domain.UseCase

import com.example.domain.Entitys.LanUserEntity
import com.example.domain.Repo.LanGamePLay
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class GetLanPlayersUseCase @Inject constructor(
    private val lanGamePLay: LanGamePLay
) {
    fun invoke() : MutableStateFlow<MutableList<LanUserEntity?>?>{
     return lanGamePLay.getLanPlayers()
    }
}