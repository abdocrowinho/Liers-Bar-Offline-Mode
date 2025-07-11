package com.example.liersbarofflinemode.ui.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.Entitys.UserEntity
import com.example.domain.UseCase.GenerateSingleGameUseCase
import com.example.domain.UseCase.GetSinglePlayerUseCase
import com.example.domain.UseCase.SinglePlayerValidation
import com.example.liersbarofflinemode.R
import com.example.liersbarofflinemode.ui.Intent.EnterSinglePlayerIntent
import com.example.liersbarofflinemode.ui.States.GunScreenState
import com.example.liersbarofflinemode.ui.States.GunShotState
import com.example.liersbarofflinemode.ui.States.LastBulletInGame
import com.example.liersbarofflinemode.ui.States.SinglePlayerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EnterSingleUserViewModel @Inject constructor(
    private val generateSingleGameUseCase: GenerateSingleGameUseCase,
    private val validation: SinglePlayerValidation,
    private val getSinglePlayerUseCase: GetSinglePlayerUseCase,

    ) : ViewModel() {

    private var _enterState = MutableStateFlow(SinglePlayerState())
    val enterState: StateFlow<SinglePlayerState> get() = _enterState

    val playerState = getSinglePlayerUseCase.invoke()

    private var _gunShotState = MutableStateFlow(GunShotState())
    val gunShotState: StateFlow<GunShotState> get() = _gunShotState


    private var _gunScreenState = MutableStateFlow<GunScreenState?>(null)
    val gunScreenState: StateFlow<GunScreenState?> get() = _gunScreenState


    private var _fireCounter = MutableStateFlow(0)
    val fireCounter: StateFlow<Int> get() = _fireCounter

    private fun clearGunShotState() {
        _gunShotState.value = GunShotState()
    }

    fun handleIntent(intent: EnterSinglePlayerIntent, navigate: () -> Unit) {
        when (intent) {
            is EnterSinglePlayerIntent.DoneButton -> {
                val error = validation.invoke(intent.userName)
                if (error.isBlank()) {
                    generateSingleGameUseCase.invoke(intent.userName)
                    val player = getSinglePlayerUseCase.invoke()
                    _enterState.value = SinglePlayerState(player = player.value)
                } else {
                    _enterState.value = SinglePlayerState(error = error)
                }

            }

            EnterSinglePlayerIntent.GunFire -> {
                viewModelScope.launch {
                    playerShootHandler()
                    delay(1200)
                    navigate()
                    clearGunShotState()
                }
            }
        }
    }

    private fun playerShootHandler() {
        val playerStateFlow = playerState
        val current = playerStateFlow.value

        if (current.isAlive) {
            _fireCounter.value++
            Log.d("gun counter in view model", _fireCounter.value.toString())

            val remaining = current.remainingBullets ?: 0
            val deadlyShot = current.numOfShot ?: -1

            val newGunState = if (remaining == deadlyShot || deadlyShot == 6) {

                GunShotState(
                    bulletStateWord = "Rest in Peace ,Mr ${current.name}",
                    moveFireBullet = true,
                    bulletVoice = R.raw.gun_shot,
                    userAfterShot = current.copy(
                        remainingBullets = 0, isAlive = false
                    )
                )
            } else {
                GunShotState(
                    bulletStateWord = "Son Of Luck , ${current.name}",
                    moveFireBullet = false,
                    bulletVoice = R.raw.dud_bullet,
                    userAfterShot = current.copy(
                        remainingBullets = remaining - 1
                    )
                )
            }
            playerStateFlow.value = newGunState.userAfterShot!!

            _gunShotState.value = newGunState
endGame(_gunShotState.value.userAfterShot!!.isAlive,_gunScreenState)
            Log.d("playerShoot", "Player $1 Shoot: ${_gunShotState.value}")
        }

    }

private fun endGame(isAlive:Boolean , mutableStateFlow: MutableStateFlow<GunScreenState?>){
    if (!isAlive){
        mutableStateFlow.value = LastBulletInGame(listOf(playerState.value))
        Log.d("gunScreenState", "playerShootHandler:${_gunScreenState.value}")

    }
}
    }
