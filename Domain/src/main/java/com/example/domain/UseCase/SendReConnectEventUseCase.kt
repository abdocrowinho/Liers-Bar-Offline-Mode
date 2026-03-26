package com.example.domain.UseCase

import com.example.domain.Repo.LanGamePLay
import jakarta.inject.Inject

class SendReconnectEventUseCase @Inject constructor(
    private val lanGamePLay: LanGamePLay
) {
    suspend fun invoke(deviceId: String, playerName: String) {
        lanGamePLay.sendReconnectEvent(deviceId, playerName)
    }
}