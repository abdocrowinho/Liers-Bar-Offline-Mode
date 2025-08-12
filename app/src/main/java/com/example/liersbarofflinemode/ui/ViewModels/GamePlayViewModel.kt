package com.example.liersbarofflinemode.ui.ViewModels
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.Entitys.UserEntity
import com.example.domain.UseCase.GetPlayersStateUseCase
import com.example.domain.UseCase.KillGameUseCase
import com.example.domain.UseCase.SetPlayersUseCase
import com.example.liersbarofflinemode.R
import com.example.liersbarofflinemode.ui.Intent.GamePlayIntent
import com.example.liersbarofflinemode.ui.States.GunScreenState
import com.example.liersbarofflinemode.ui.States.GunShotState
import com.example.liersbarofflinemode.ui.States.LastBulletInGame
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class GamePlayViewModel @Inject constructor(
    private val setPlayersUseCase: SetPlayersUseCase,
    private var getPlayersUseCase: GetPlayersStateUseCase
    ,private val killGameUseCase: KillGameUseCase


) : ViewModel(

) {



    private var _gunShotState = MutableStateFlow<Map<Int, GunShotState>>(emptyMap())
    val gunShotState: StateFlow<Map<Int, GunShotState>> get() = _gunShotState

    private var _gunScreenState = MutableStateFlow<GunScreenState?>(null)
    val gunScreenState: StateFlow<GunScreenState?> get() = _gunScreenState

    private var _deadCounter = MutableStateFlow(0)
    private val _playerOlder = MutableStateFlow(ArrayDeque<UserEntity>())
  private  var _playersState = getPlayersUseCase.getGamePlayRepo()
    private var _fireCounter = MutableStateFlow(0)
    val fireCounter : StateFlow<Int> get() = _fireCounter

    val playersState : Map<Int, MutableStateFlow<UserEntity?>>get() = _playersState
    fun clearGunShotState() {
        val updatedMap = _gunShotState.value.toMutableMap()
        updatedMap.clear()
        _gunShotState.value = updatedMap
        _fireCounter.value = 0
    }
  fun zeroingDeadCounter(){
     _deadCounter.value = 0
     _playerOlder.value = ArrayDeque()
     _gunScreenState.value = GunShotState()

 }

    fun setPlayers(players: List<UserEntity>) {
        if (players.size != 4 || _playersState.values.any { it.value != null }) return
        setPlayersUseCase.invoke(players)
    }
    fun handleIntent(intent: GamePlayIntent, navigate: () -> Unit) {
        when (intent) {

            is GamePlayIntent.FireBullet -> {
                viewModelScope.launch {
                    playerShootHandler(intent.index)
                    delay(1200)
                    navigate()
                }
            }
        }
    }

    private  fun playerShootHandler(playerIndex: Int) {
        val playerStateFlow = _playersState[playerIndex] ?: return
        val current = playerStateFlow.value ?: return

        if (current.isAlive) {
            _fireCounter.value++
            Log.d("gun counter in view model", _fireCounter.value.toString())

            val remaining = current.remainingBullets ?: 0
            val deadlyShot = current.numOfShot ?: -1
            val newGunState = if (remaining == deadlyShot || deadlyShot == 6) {

                _deadCounter.value++
                _playerOlder.value.add(current)
                endGame(_deadCounter.value,_playersState,_playerOlder,_gunScreenState)
               GunShotState(
                bulletStateWord = "Rest in Peace ,Mr ${current.name}",
                    moveFireBullet = true,
                    bulletVoice = R.raw.gun_shot,
                    userAfterShot = current.copy(remainingBullets = 0, isAlive = false
                    )
                )
            } else {
                GunShotState(
                    bulletStateWord = "luck Guy , ${current.name}",
                    moveFireBullet = false,
                    bulletVoice = R.raw.dud_bullet,
                    userAfterShot = current.copy(
                        remainingBullets = remaining - 1
                    )
                )
            }
            playerStateFlow.value = newGunState.userAfterShot

            _gunShotState.value = _gunShotState.value.toMutableMap().apply {
                put(playerIndex, newGunState)
            }

            Log.d("playerShoot", "Player $playerIndex Shoot: ${_gunShotState.value}")
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("when view model killed ", "taaaakh")
        killGameUseCase.invoke()
    }


}
private fun endGame(deadCounter:Int , playersState: Map<Int, MutableStateFlow<UserEntity?>>,
                    playerOlder: MutableStateFlow<ArrayDeque<UserEntity>>,
                    gunScreenState : MutableStateFlow<GunScreenState?>
                    ) {
    if (deadCounter == 3) {
        val playerWinner = playersState.values.filter { it.value!!.remainingBullets != 0 }
        Log.d("playerWinner", playerWinner.toString())
        playerOlder.value.add(playerWinner.first().value!!)
        Log.d("playersOlderList", "${playerOlder.value} ")
        gunScreenState.value = LastBulletInGame(playerOlder.value)
    } else {
        gunScreenState.value = null
    }

}