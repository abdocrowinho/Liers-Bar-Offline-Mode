package com.example.liersbarofflinemode.ui.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.domain.UseCase.GetPlayersStateUseCase
import com.example.domain.UseCase.KillGameUseCase
import com.example.liersbarofflinemode.ui.Intent.EndGameDialogIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EndGameDialogViewModel @Inject constructor(
    private val getPlayersUseCase: GetPlayersStateUseCase
) : ViewModel() {

    fun handleIntent(intent: EndGameDialogIntent) {
        when (intent) {
            is EndGameDialogIntent.PlayAgainIntent -> playAgain(intent.onPlayAgain)
            is EndGameDialogIntent.Back -> backToLoopy(intent.onBackAction)
        }

    }

    private fun backToLoopy(onBackAction: () -> Unit) {
        onBackAction()
    }

    private fun playAgain(navigate: () -> Unit ) {
        getPlayersUseCase.getGamePlayRepo() .values.forEachIndexed{
                i , player ->
            Log.d("player$i before", player.value.toString())
        }

        getPlayersUseCase.getGamePlayRepo() .forEach{(_, player )->
           val current = player.value
       val updatePlayer = current?.copy(remainingBullets = 6, numOfShot = (1..6).random() , isAlive = true)
player.value = updatePlayer
           }
        getPlayersUseCase.getGamePlayRepo().values.forEachIndexed{
    i , player ->
    Log.d("player$i after", player.value.toString())
}
        navigate()
    }
}

