package com.example.domain.UseCase

import com.example.domain.GameEvents.Event
import com.example.domain.Repo.LanGamePLay
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetMessageEventUseCase
@Inject constructor(
    private val lanGamePLay: LanGamePLay
){
    suspend fun  invoke():SharedFlow<Event?>{
        return lanGamePLay.getMessage()
    }
}