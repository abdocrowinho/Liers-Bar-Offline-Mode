package com.example.domain.UseCase

import com.example.domain.GameEvents.Event
import com.example.domain.Repo.LanGamePLay
import javax.inject.Inject

class SendEventUseCase @Inject constructor(
    private val lanGamePLay: LanGamePLay
) {
fun invoke (event: Event){
    lanGamePLay.sendEvent(event)
}
}