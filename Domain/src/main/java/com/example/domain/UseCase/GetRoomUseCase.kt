package com.example.domain.UseCase

import com.example.domain.Entitys.RoomEntity
import com.example.domain.Entitys.UserEntity
import com.example.domain.Repo.LanGamePLay
import com.example.domain.Utlites.UiResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRoomUseCase @Inject constructor(
    private val lanGamePLay: LanGamePLay
) {
     fun invoke(player: String) : Flow<UiResult<RoomEntity?>>  {
        return lanGamePLay.getRoom()
}}