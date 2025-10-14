package com.example.liersbarofflinemode.ui.ViewModels

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.DataSource.localeDataSource.LanServeis.WebSocketServerManger
import com.example.domain.Entitys.Card
import com.example.domain.Entitys.LanUserEntity
import com.example.domain.GameEvents.CardPlayEvent
import com.example.domain.GameEvents.Event
import com.example.domain.GameEvents.JoinToGameEvent
import com.example.domain.GameEvents.LiarCallEvent
import com.example.domain.GameEvents.LiarCallResult
import com.example.domain.GameEvents.PlayerShotEvent
import com.example.domain.GameEvents.RoomStateEvent
import com.example.domain.GameEvents.StartRoundEvent
import com.example.domain.GameEvents.WarningEvent
import com.example.domain.UseCase.GetLanPlayersUseCase
import com.example.domain.UseCase.GetMessageEventUseCase
import com.example.domain.UseCase.SendEventUseCase
import com.example.domain.Utlites.LanPlayerState
import com.example.domain.Utlites.MangerLanPlayerState
import com.example.domain.Utlites.getMyIpAddress
import com.example.liersbarofflinemode.R
import com.example.liersbarofflinemode.ui.Intent.EventsLanGamePlayIntent
import com.example.liersbarofflinemode.ui.Intent.LanGamePlayIntent
import com.example.liersbarofflinemode.ui.States.TablePlayersState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LanGamePlayViewModel @Inject constructor(
    private val getLanPlayersUseCase: GetLanPlayersUseCase,
    private val sendEventUseCase: SendEventUseCase,
    private val getMessageEventUseCase: GetMessageEventUseCase
) : ViewModel() {
    private var _players = MutableStateFlow<MutableList<LanUserEntity?>?>(null)
    val players: StateFlow<MutableList<LanUserEntity?>?> get() = _players
    private var _readyCard = MutableStateFlow<MutableList<Card>>(mutableListOf())
    val readyCard: StateFlow<MutableList<Card>> get() = _readyCard
    private var activePlayer: LanUserEntity? = null

    private val _uiState = MutableStateFlow(TablePlayersState())
    val uiState: StateFlow<TablePlayersState> = _uiState.asStateFlow()

    private var _oneShot = MutableSharedFlow<UiOneShot>(replay = 0)
    val oneShot = _oneShot.asSharedFlow()


    private var activePlayerState = MangerLanPlayerState.state.value.name


    init {
        viewModelScope.launch {
            MangerLanPlayerState.state.collect {
                Log.d("VM-StateCollect", "Collected state = $it")
                activePlayerState = it.name
                updatePlayers(it.name)
                handleEventMessages()
            }
        }
    }


    private fun handleEventMessages() {
        viewModelScope.launch {
            if (activePlayerState == LanPlayerState.Host.name) {
                WebSocketServerManger.getServer()?.serverEventState?.collect {
                    if (it != null) {
                        Log.d("event", "$it")
                        handleUiState(it)
                    }
                }

            } else {
                getMessageEventUseCase.invoke().collect { event ->
                    Log.d("VM", "Event received in VM: $event")
                    if (event != null) {
                        Log.d("event", "$event")
                        handleUiState(event)
                    }
                }
            }
        }

    }

    fun handelIntentCards(intent: LanGamePlayIntent) {
        when (intent) {
            is LanGamePlayIntent.EmitCardsAreReady -> {
                // must implement
            }

            is LanGamePlayIntent.AddReadyCard -> {
                val currentList = _readyCard.value.toMutableList()
                if (currentList.contains(intent.card)) return
                else currentList.add(intent.card)

                _readyCard.value = currentList
                Log.d(
                    "ready card to List", _readyCard.value.toString()
                )
            }

            is LanGamePlayIntent.RemoveReadyCard -> {
                val currentList = _readyCard.value.toMutableList()
                currentList.removeAll { it == intent.card }
                _readyCard.value = currentList
                Log.d("delete ready card from List", _readyCard.value.toString())

            }

        }
    }

    fun handleEvents(eventIntent: EventsLanGamePlayIntent) {
        when (eventIntent) {
            is EventsLanGamePlayIntent.CallLiarButton -> {
                if (activePlayer?.isHost != false) {
                    viewModelScope.launch {
                        WebSocketServerManger.getServer()?.hostLiarCall()
                        delay(1200)
                        WebSocketServerManger.getServer()?.hostLiarCallProcess()

                    }
                } else {
                    sendEventUseCase.invoke(
                        LiarCallEvent(
                            callerId = activePlayer?.id ?: 1,
                        )
                    )
                }

            }

            EventsLanGamePlayIntent.DealCards -> {
                if (activePlayerState == LanPlayerState.Host.name) {
                    WebSocketServerManger.getServer()?.startNewRound()
                } else {
                    sendEventUseCase.invoke(StartRoundEvent)
                }
            }

            is EventsLanGamePlayIntent.ThrowCardsButton -> {

                val cardPlayEvent = CardPlayEvent(
                    eventIntent.playerId,
                    card = _readyCard.value,
                    nextPlayer = nextPlayer(eventIntent.playerId),
                    index = _readyCard.value.size
                )

                if (activePlayerState == LanPlayerState.Host.name) {
                    WebSocketServerManger.getServer()?.hostPlayCard(_readyCard.value)
                    Log.d(
                        "viewModelPlayerCards",
                        _players.value?.find { it?.ipAddress == getMyIpAddress() }?.cards?.size.toString()
                    )
                } else {
                    sendEventUseCase.invoke(
                        cardPlayEvent
                    )
                }
                _readyCard.value = mutableListOf()

            }

            EventsLanGamePlayIntent.Warning -> {
                if (activePlayerState == LanPlayerState.Host.name) {
                    WebSocketServerManger.getServer()?.hostPlayerWarning()
                } else {
                    sendEventUseCase.invoke(WarningEvent)

                }
            }
        }
    }

    private suspend fun handleUiState(event: Event) {
        when (event) {
            is CardPlayEvent -> onCardPlayed(event)
            is JoinToGameEvent -> onJoinToGame(event)
            is LiarCallEvent -> onLiarCall(event)
            is LiarCallResult -> onLiarCallResult(event)
            is PlayerShotEvent -> TODO()
            is RoomStateEvent -> onRoomState(event)
            StartRoundEvent -> onStartRound(event)
            WarningEvent -> onWarning()
        }

    }

    private suspend fun onWarning() {
        _uiState.update {
            it.copy(isWarning = true)
        }
        _oneShot.emit(UiOneShot.PlayWarningSound)

    }


    private fun onStartRound(event: Event) {
    }

    private fun onRoomState(event: RoomStateEvent) {
        if (event.tableBase != null) {
            Log.d("tableBase = ", event.tableBase.toString())
            _uiState.update { s ->
                s.copy(
                    tableBase = event.tableBase?.rank,
                    roundCounter = event.round ?: 0,
                    isMyTurn = activePlayer?.id == event.round,
                )
            }
        }

    }

    private suspend fun onLiarCallResult(event: LiarCallResult) {
        val looser = _players.value?.get(event.loserId.minus(1))
        val bulletWordState = if (event.isRealBullet) {
            "Rest In Peace${looser?.name}"
        } else {
            "Lucky Guy , ${looser?.name}"
        }
        val gunSound = if (event.isRealBullet) {
            R.raw.gun_shot
        } else {
            R.raw.dud_bullet
        }

        _uiState.update { s ->
            s.copy(
                bulletWordState = bulletWordState,
                cardsUnderTest = event.cardsRank,
                isRealBullet = event.isRealBullet,
                looser = looser,
                gunSound = gunSound,
                spokenText = ""
            )
        }
        _oneShot.emit(UiOneShot.Speak(bulletWordState))

    }

    private suspend fun onLiarCall(event: LiarCallEvent) {
        _oneShot.emit(UiOneShot.Speak("liar"))
        _uiState.update {
            it.copy(
                spokenText = "liar", isPlayerCallingLiar = true
            )
        }

    }


    private fun onJoinToGame(event: JoinToGameEvent) {

    }

    private suspend fun onCardPlayed(event: CardPlayEvent) {
        val tableBase = WebSocketServerManger.getServer()?.baseTable?.value
        _uiState.update { s ->
            s.copy(
                cardCountState = event.card.size,
                showCardsState = true,
                cardPlayerIdState = event.playerId,
                isFindCardsInTable = true,
                isMyTurn = activePlayer?.id == event.nextPlayer,
                spokenText = "${event.card.size} ${tableBase?.rank?.name ?: ""}"

            )
        }
        _oneShot.emit(UiOneShot.Speak("${event.card.size} ${tableBase?.rank?.name ?: ""}"))

    }

    private fun nextPlayer(id: Int): Int {
        val handleId = if (id == 4) 1 else {
            id.plus(1)
        }
        val player = players.value?.find { it?.id == handleId }
        return if (player?.isAlive != false) {
            handleId
        } else {
            nextPlayer(handleId)
        }
    }

    fun clearShowCards() {
        _uiState.update {
            it.copy(
                showCardsState = false,
                cardCountState = 0,
                cardPlayerIdState = -1,
            )
        }
    }

    fun clearWarning() {

    }

    fun clearPlayerCallLiar() {
        _uiState.update {
            it.copy(
                isPlayerCallingLiar = false,
                cardsUnderTest = listOf()
            )
        }
    }

    fun clearSpokenText() {
        _uiState.update {
            it.copy(
                spokenText = ""
            )
        }
    }

    fun updatePlayers(playerState: String) {
        Log.d("playerState : ->", playerState)

        viewModelScope.launch {
            if (playerState == LanPlayerState.Host.name) {
                launch {
                    getLanPlayersUseCase.invoke().collect { players ->

                        _players.value = players?.toMutableList()
                        Log.d("players in room", _players.value.toString())
                        activePlayer = _players.value?.find { it?.ipAddress == getMyIpAddress() }

                    }
                }

            } else if (playerState == LanPlayerState.Client.name) {
                getMessageEventUseCase.invoke().collect { event ->
                    Log.d("event in room VM", event.toString())

                    if (event is RoomStateEvent) {

                        _players.value = event.playersInRoom.toMutableList()
                        activePlayer =
                            _players.value?.find { it?.ipAddress == getMyIpAddress() }
                    }
                }
            }
        }
    }

    fun updateLooserPlayer(isRealBullet: Boolean) {
        Log.d("loser player updating in VM", isRealBullet.toString())
        WebSocketServerManger.getServer()?.updateLooserPlayer(isRealBullet)
    }

    sealed class UiOneShot {
        data class Speak(var text: String) : UiOneShot()
        data object PlayWarningSound : UiOneShot()
    }

}