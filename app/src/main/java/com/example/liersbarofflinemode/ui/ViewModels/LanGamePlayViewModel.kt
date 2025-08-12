package com.example.liersbarofflinemode.ui.ViewModels

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.Entitys.Card
import com.example.domain.Entitys.LanUserEntity
import com.example.domain.UseCase.GetLanPlayersUseCase
import com.example.liersbarofflinemode.ui.Intent.EventsLanGamePlayIntent
import com.example.liersbarofflinemode.ui.Intent.LanGamePlayIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LanGamePlayViewModel @Inject constructor(
 private   val getLanPlayersUseCase: GetLanPlayersUseCase
) : ViewModel() {
    private var _players = MutableStateFlow<List<LanUserEntity?>?>(null)
    val players : StateFlow< List< LanUserEntity?>?> get() = _players
    private var  _readyCard= MutableStateFlow<MutableList<Card>>(mutableListOf())
    val  readyCard : StateFlow<MutableList<Card>> get() = _readyCard
    private var  _listReadyCards= MutableStateFlow<MutableList<Card>>(mutableListOf())
    val  listReadyCards : StateFlow<List<Card>>get() = _listReadyCards



    init {
        viewModelScope.launch {
            getLanPlayersUseCase.invoke().collect{ players->
                _players.value = players
                Log.d("players in room", _players.value.toString())

            }
        }
    }

    fun handelIntentCards(intent: LanGamePlayIntent ){
        when(intent){
            is LanGamePlayIntent.EmitCardsAreReady -> TODO()
            is LanGamePlayIntent.AddReadyCard -> {
                val currentList = _readyCard.value.toMutableList()
                 if (currentList.contains(intent.card) )return

                else  currentList.add(intent.card)

                _readyCard.value = currentList
                Log.d( "ready card to List", _readyCard.value.toString()
                )
            }

            is LanGamePlayIntent.RemoveReadyCard -> {
                val currentList = _readyCard.value.toMutableList()
                currentList.removeAll { it ==intent.card }
                _readyCard.value =currentList
                Log.d( "delete ready card from List", _readyCard.value.toString())

            }
        }
    }
    fun handleEvents(eventIntent : EventsLanGamePlayIntent){
        when(eventIntent){
            is EventsLanGamePlayIntent.CallLiarButton -> TODO()
            EventsLanGamePlayIntent.DealCards -> TODO()
            is EventsLanGamePlayIntent.ThrowCardsButton -> TODO()
        }
    }
}