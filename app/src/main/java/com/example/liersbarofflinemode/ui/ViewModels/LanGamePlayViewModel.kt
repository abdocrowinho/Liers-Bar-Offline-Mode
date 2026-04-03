package com.example.liersbarofflinemode.ui.ViewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.DataSource.localeDataSource.LanServeis.WebSocketServerManger
import com.example.data.DataSource.localeDataSource.deviceIdManger.DeviceIdManager
import com.example.domain.Entitys.Card
import com.example.domain.Entitys.LanUserEntity
import com.example.domain.GameEvents.BotVoteEvent
import com.example.domain.GameEvents.BotVoteStateEvent
import com.example.domain.GameEvents.CardPlayEvent
import com.example.domain.GameEvents.Event
import com.example.domain.GameEvents.GameOverEvent
import com.example.domain.GameEvents.JoinToGameEvent
import com.example.domain.GameEvents.LiarCallEvent
import com.example.domain.GameEvents.LiarCallResult
import com.example.domain.GameEvents.PlayAgainEvent
import com.example.domain.GameEvents.PlayerShotEvent
import com.example.domain.GameEvents.RoomStateEvent
import com.example.domain.GameEvents.StartRoundEvent
import com.example.domain.GameEvents.WarningEvent
import com.example.domain.UseCase.GetLanPlayersUseCase
import com.example.domain.UseCase.GetMessageEventUseCase
import com.example.domain.UseCase.SendEventUseCase
import com.example.domain.UseCase.SendReconnectEventUseCase
import com.example.domain.Utlites.LanPlayerState
import com.example.domain.Utlites.MangerLanPlayerState
import com.example.domain.Utlites.getMyIpAddress
import com.example.liersbarofflinemode.R
import com.example.liersbarofflinemode.ui.Intent.EventsLanGamePlayIntent
import com.example.liersbarofflinemode.ui.Intent.LanGamePlayIntent
import com.example.liersbarofflinemode.ui.States.TablePlayersState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LanGamePlayViewModel @Inject constructor(
    private val getLanPlayersUseCase: GetLanPlayersUseCase,
    private val sendEventUseCase: SendEventUseCase,
    private val getMessageEventUseCase: GetMessageEventUseCase,
    private val sendReconnectEventUseCase: SendReconnectEventUseCase,
    @param:ApplicationContext val context: Context
) : ViewModel() {

    //State

    private val _players = MutableStateFlow<MutableList<LanUserEntity?>?>(null)
    val players: StateFlow<MutableList<LanUserEntity?>?> get() = _players

    private val _readyCard = MutableStateFlow<MutableList<Card>>(mutableListOf())
    val readyCard: StateFlow<MutableList<Card>> get() = _readyCard

    private var activePlayer: LanUserEntity? = null

    private val _uiState = MutableStateFlow(TablePlayersState())
    val uiState: StateFlow<TablePlayersState> = _uiState.asStateFlow()

    private val _oneShot = MutableSharedFlow<UiOneShot>(replay = 0)
    val oneShot: SharedFlow<UiOneShot> = _oneShot.asSharedFlow()

    private var activePlayerState = MangerLanPlayerState.state.value.name

    // Real countdown timer
    private var timerJob: Job? = null
    private val _timerValue = MutableStateFlow(0)
    val timerValue: StateFlow<Int> = _timerValue.asStateFlow()

    private val sharedClientEvents = MutableSharedFlow<Event?>(replay = 0)


    init {
        viewModelScope.launch {
            getMessageEventUseCase.invoke().collect { event ->
                sharedClientEvents.emit(event)
            }
        }

        viewModelScope.launch {
            MangerLanPlayerState.state.collect { state ->
                Log.d("VM-StateCollect", "role changed to=$state")
                activePlayerState = state.name
                observePlayers()
            }
        }

        observeEvents()

        if (activePlayerState == LanPlayerState.Client.name) {
            viewModelScope.launch {
                sendReconnectEventUseCase.invoke(
                    deviceId = DeviceIdManager.getDeviceId(context),
                    playerName = MangerLanPlayerState.playerName
                )
            }
        }
    }

    //Observers

    private fun observePlayers() {
        viewModelScope.launch {
            if (activePlayerState == LanPlayerState.Host.name) {
                getLanPlayersUseCase.invoke().collect { players ->
                    _players.value = players?.toMutableList()
                    activePlayer = _players.value?.find { it?.ipAddress == getMyIpAddress() }
                }
            } else {
                sharedClientEvents.collect { event ->
                    if (event is RoomStateEvent) {
                        _players.value = event.playersInRoom.toMutableList()
                        activePlayer = _players.value?.find { it?.ipAddress == getMyIpAddress() }
                    }
                }
            }
        }
    }

    private fun observeEvents() {
        viewModelScope.launch {
            if (activePlayerState == LanPlayerState.Host.name) {
                WebSocketServerManger.getServer()?.serverEventState?.collect { event ->
                    handleUiState(event)
                }
            } else {
                sharedClientEvents.collect { event ->
                    if (event != null) handleUiState(event)
                }
            }
        }
    }

    // Intents

    fun handelIntentCards(intent: LanGamePlayIntent) {
        when (intent) {
            is LanGamePlayIntent.EmitCardsAreReady -> {}
            is LanGamePlayIntent.AddReadyCard -> {
                val current = _readyCard.value.toMutableList()
                if (current.contains(intent.card)) return
                current.add(intent.card)
                _readyCard.value = current
            }

            is LanGamePlayIntent.RemoveReadyCard -> {
                val current = _readyCard.value.toMutableList()
                current.removeAll { it == intent.card }
                _readyCard.value = current
            }
        }
    }

    fun handleEvents(eventIntent: EventsLanGamePlayIntent) {
        when (eventIntent) {
            is EventsLanGamePlayIntent.ThrowCardsButton -> {
                val cardsToSend = _readyCard.value.ifEmpty {
                    activePlayer?.cards?.shuffled()?.take(1) ?: emptyList()
                }
                if (activePlayerState == LanPlayerState.Host.name) {
                    WebSocketServerManger.getServer()?.hostPlayCard(cardsToSend)
                } else {
                    sendEventUseCase.invoke(
                        CardPlayEvent(
                            playerId = activePlayer?.id ?: 1,
                            card = cardsToSend,
                            nextPlayer = 0,
                            index = cardsToSend.size
                        )
                    )
                }
                _readyCard.value = mutableListOf()
            }

            is EventsLanGamePlayIntent.CallLiarButton -> {
                if (activePlayerState == LanPlayerState.Host.name) {
                    viewModelScope.launch {
                        WebSocketServerManger.getServer()?.hostLiarCall()
                        delay(1200)
                        WebSocketServerManger.getServer()?.hostLiarCallProcess()
                    }
                } else {
                    sendEventUseCase.invoke(LiarCallEvent(callerId = activePlayer?.id ?: 1))
                }
            }

            is EventsLanGamePlayIntent.DealCards -> {
                if (activePlayerState == LanPlayerState.Host.name) {
                    WebSocketServerManger.getServer()?.startNewRound()
                } else {
                    sendEventUseCase.invoke(StartRoundEvent)
                }
            }

            is EventsLanGamePlayIntent.Warning -> {
                if (activePlayerState == LanPlayerState.Host.name) {
                    WebSocketServerManger.getServer()?.hostPlayerWarning()
                } else {
                    sendEventUseCase.invoke(WarningEvent)
                }
            }
        }
    }

    // UI State Handlers

    private suspend fun handleUiState(event: Event) {
        when (event) {
            is CardPlayEvent -> onCardPlayed(event)
            is RoomStateEvent -> onRoomState(event)
            is LiarCallEvent -> onLiarCall(event)
            is LiarCallResult -> onLiarCallResult(event)
            is BotVoteStateEvent -> onBotVoteState(event)
            is GameOverEvent -> onGameOver(event)
            is PlayAgainEvent -> onPlayAgain()
            is WarningEvent -> onWarning()
            is StartRoundEvent -> _readyCard.value = mutableListOf()
            is JoinToGameEvent -> {}
            is PlayerShotEvent -> {}
            else -> {}
        }
    }

    private fun onRoomState(event: RoomStateEvent) {
        val isMyTurn = activePlayer?.id == event.currentTurnId
        _uiState.update { s ->
            s.copy(
                tableBase = event.baseTable?.rank,
                roundCounter = event.round ?: 0,
                currentTurnId = event.currentTurnId,
                isMyTurn = isMyTurn
            )
        }

        // Restart timer on every turn change
        restartTimer(isMyTurn = isMyTurn)

        val humanCount = event.playersInRoom.count { it?.isBot == false }
        val shouldTriggerVote = humanCount in 2..3
                && activePlayerState == LanPlayerState.Host.name
                && (_uiState.value.roundCounter == 0)
                && !_uiState.value.showBotVoteDialog

        if (shouldTriggerVote) {
            _uiState.update { it.copy(showBotVoteDialog = true, botVoteTotalPlayers = humanCount) }
            WebSocketServerManger.getServer()?.apply {
                botVoteActive.value = true
                botVoteYesCount.value = 0
                broadcastBotVoteDialog(humanCount)
            }
        }
    }

    private suspend fun onCardPlayed(event: CardPlayEvent) {
        val tableBaseName = _uiState.value.tableBase?.name ?: ""
        val spokenText = "${event.card.size} $tableBaseName"
        val isMyTurn = activePlayer?.id == event.nextPlayer

        _uiState.update { s ->
            s.copy(
                cardCountState = event.card.size,
                showCardsState = true,
                cardPlayerIdState = event.playerId,

                isFindCardsInTable = true,
                currentTurnId = event.nextPlayer,
                isMyTurn = isMyTurn,
                spokenText = spokenText
            )
        }

        // Restart timer for whoever's turn is next
        restartTimer(isMyTurn = isMyTurn)

        _oneShot.emit(UiOneShot.Speak(spokenText))
    }

    private suspend fun onLiarCallResult(event: LiarCallResult) {
        val loser = _players.value?.find { it?.id == event.loserId }
        val bulletsBeforeShot = loser?.remainingBullets ?: 6
        val bulletWordState = if (event.isRealBullet) "Rest In Peace ${loser?.name}"
        else "Lucky Guy, ${loser?.name}"
        val gunSound = if (event.isRealBullet) R.raw.gun_shot else R.raw.dud_bullet

        _uiState.update { s ->
            s.copy(
                bulletWordState = bulletWordState,
                cardsUnderTest = event.cardsRank,
                isRealBullet = event.isRealBullet,
                looser = loser,
                gunSound = gunSound,
                spokenText = "",
                isMyTurn = false,
                bulletsBeforeShot = bulletsBeforeShot,
                isFindCardsInTable = false
            )
        }

        stopTimer()

        delay(4000)
        if (activePlayerState == LanPlayerState.Host.name) {
            WebSocketServerManger.getServer()?.startNewRound()
        }
    }

    private suspend fun onLiarCall(event: LiarCallEvent) {
        _uiState.update { it.copy(spokenText = "liar", isPlayerCallingLiar = true) }
        _oneShot.emit(UiOneShot.Speak("liar"))
    }

    private fun onGameOver(event: GameOverEvent) {
        stopTimer()
        _uiState.update { s ->
            s.copy(
                isGameOver = true,
                winnerName = event.winnerName,
                winnerId = event.winnerId,
                isMyTurn = false,
                isFindCardsInTable = false
            )
        }
    }

    private fun onPlayAgain() {
        stopTimer()
        _readyCard.value = mutableListOf()
        _uiState.update { TablePlayersState() }
    }

    private suspend fun onWarning() {
        _uiState.update { it.copy(isWarning = true) }
        _oneShot.emit(UiOneShot.PlayWarningSound)
    }

    // timer

    private fun restartTimer(isMyTurn: Boolean) {
        timerJob?.cancel()
        _timerValue.value = 0
        if (!isMyTurn) return

        timerJob = viewModelScope.launch {
            for (i in 10 downTo 0) {
                _timerValue.value = i
                if (i == 0) {
                    autoThrow()
                    break
                }
                delay(1000L)
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
        _timerValue.value = 0
    }

    private fun autoThrow() {
        val cards = activePlayer?.cards ?: return
        if (cards.isEmpty()) return
        val cardToThrow = cards.shuffled().take(1)
        if (activePlayerState == LanPlayerState.Host.name) {
            WebSocketServerManger.getServer()?.hostPlayCard(cardToThrow)
        } else {
            sendEventUseCase.invoke(
                CardPlayEvent(
                    playerId = activePlayer?.id ?: 1,
                    card = cardToThrow,
                    nextPlayer = 0,
                    index = cardToThrow.size
                )
            )
        }
        _readyCard.value = mutableListOf()
    }

    // Bot Voting

    private fun onBotVoteState(event: BotVoteStateEvent) {
        _uiState.update { s ->
            when {
                event.showDialog -> s.copy(showBotVoteDialog = true, botVoteTotalPlayers = event.totalPlayers)
                event.accepted || event.rejected -> s.copy(showBotVoteDialog = false, botVoteYesCount = 0)
                else -> s.copy(botVoteYesCount = event.yesCount, botVoteTotalPlayers = event.totalPlayers)
            }
        }
    }

    fun submitBotVote(vote: Boolean) {
        val playerId = activePlayer?.id ?: return
        if (activePlayerState == LanPlayerState.Host.name) {
            WebSocketServerManger.getServer()?.serverHandler?.handle(
                conn = null,
                event = BotVoteEvent(playerId = playerId, vote = vote)
            )
        } else {
            sendEventUseCase.invoke(BotVoteEvent(playerId = playerId, vote = vote))
        }
        _uiState.update { it.copy(showBotVoteDialog = false) }
    }

    // Play Again

    fun handlePlayAgain() {
        if (activePlayerState == LanPlayerState.Host.name) {
            WebSocketServerManger.getServer()?.hostPlayAgain()
        } else {
            sendEventUseCase.invoke(PlayAgainEvent)
        }
    }

    // UI Clear Helpers

    fun clearShowCards() {
        _uiState.update { it.copy(showCardsState = false, cardCountState = 0, cardPlayerIdState = -1) }
    }

    fun clearWarning() = _uiState.update { it.copy(isWarning = false) }
    fun clearPlayerCallLiar() = _uiState.update { it.copy(isPlayerCallingLiar = false, cardsUnderTest = listOf()) }
    fun clearSpokenText() = _uiState.update { it.copy(spokenText = "") }

    // One Shot Events

    sealed class UiOneShot {
        data class Speak(val text: String) : UiOneShot()
        data object PlayWarningSound : UiOneShot()
    }
}