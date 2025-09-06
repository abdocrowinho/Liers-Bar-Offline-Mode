package com.example.liersbarofflinemode.ui.ViewModels

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.Entitys.Card
import com.example.domain.Entitys.LanUserEntity
import com.example.domain.GameEvents.CardPlayEvent
import com.example.domain.GameEvents.Event
import com.example.domain.GameEvents.LiarCallEvent
import com.example.domain.GameEvents.StartRoundEvent
import com.example.domain.GameEvents.WarningEvent
import com.example.domain.UseCase.GetLanPlayersUseCase
import com.example.domain.UseCase.GetMessageEventUseCase
import com.example.domain.UseCase.SendEventUseCase
import com.example.liersbarofflinemode.ui.Intent.EventsLanGamePlayIntent
import com.example.liersbarofflinemode.ui.Intent.LanGamePlayIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LanGamePlayViewModel @Inject constructor(
 private   val getLanPlayersUseCase: GetLanPlayersUseCase,
    private val sendEventUseCase: SendEventUseCase,
    private val getMessageEventUseCase: GetMessageEventUseCase
) : ViewModel() {
    private var _players = MutableStateFlow<MutableList<LanUserEntity?>?>(null)
    val players : StateFlow< MutableList< LanUserEntity?>?> get() = _players
    private var  _readyCard= MutableStateFlow<MutableList<Card>>(mutableListOf())
    val  readyCard : StateFlow<MutableList<Card>> get() = _readyCard

private var _eventMessage = MutableSharedFlow<Event?>()
    val eventChannel : SharedFlow<Event?>get()= _eventMessage

    init {
        viewModelScope.launch {
            getLanPlayersUseCase.invoke().collect{ players->
                _players.value = players
                Log.d("players in room", _players.value.toString())

            }
        }
        viewModelScope.launch {
            getMessageEventUseCase.invoke().collect { event ->
                    _eventMessage.emit(event)
                    Log.d("VM", "Event received in VM: $event")

            }
        }

    }

    fun handelIntentCards(intent: LanGamePlayIntent ){
        when(intent){
            is LanGamePlayIntent.EmitCardsAreReady -> {
                // must implement
            }
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
            is EventsLanGamePlayIntent.CallLiarButton -> {
            }
            EventsLanGamePlayIntent.DealCards -> {

                sendEventUseCase.invoke(StartRoundEvent)

            }
            is EventsLanGamePlayIntent.ThrowCardsButton -> {
                val nextPlayer = if (eventIntent.playerId==4) 1 else{eventIntent.playerId.plus(1)}
                val cardPlayEvent=  CardPlayEvent(eventIntent.playerId,
                    card = _readyCard.value,
                    nextPlayer = nextPlayer ,
                    index = _readyCard.value.size
                )
                Log.d("client-> CardPlayEvent = " , cardPlayEvent.toString())
                sendEventUseCase.invoke(
                  cardPlayEvent
                )
                _readyCard.value = mutableListOf()

            }

            EventsLanGamePlayIntent.Warning -> {
                sendEventUseCase.invoke(WarningEvent)
            }
        }
    }
}