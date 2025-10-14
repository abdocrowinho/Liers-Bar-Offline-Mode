package com.example.liersbarofflinemode.ui.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.DataSource.localeDataSource.LanServeis.GameWebSocketServer
import com.example.data.DataSource.localeDataSource.LanServeis.WebSocketServerManger
import com.example.domain.Entitys.LanUserEntity
import com.example.domain.Entitys.RoomEntity
import com.example.domain.GameEvents.JoinToGameEvent
import com.example.domain.UseCase.ConnectToRoomUseCase
import com.example.domain.UseCase.GetConnectionStatusUseCase
import com.example.domain.UseCase.GetRoomUseCase
import com.example.domain.UseCase.JoinGameUseCase
import com.example.domain.UseCase.SendEventUseCase
import com.example.domain.UseCase.StartRoomUseCase
import com.example.domain.Utlites.LanPlayerState
import com.example.domain.Utlites.MangerLanPlayerState
import com.example.domain.Utlites.UiResult
import com.example.domain.Utlites.getMyIpAddress
import com.example.liersbarofflinemode.ui.Intent.StartScreenIntent
import com.example.liersbarofflinemode.ui.States.Error
import com.example.liersbarofflinemode.ui.States.HideLanDialog
import com.example.liersbarofflinemode.ui.States.HideRooms
import com.example.liersbarofflinemode.ui.States.Loading
import com.example.liersbarofflinemode.ui.States.NavigationState
import com.example.liersbarofflinemode.ui.States.ShowLanDialog
import com.example.liersbarofflinemode.ui.States.StartScreenState
import com.example.liersbarofflinemode.ui.States.Success
import com.example.liersbarofflinemode.ui.States.Timeout
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.ws.WebSocketProtocol
import java.net.ServerSocket
import javax.inject.Inject

@HiltViewModel
class StartScreenViewModel @Inject constructor(
    private val createRoomUseCase: StartRoomUseCase,
    private val getRoomUseCase: GetRoomUseCase,
    private val connectToRoomUseCase: ConnectToRoomUseCase,
    private val sendEventUseCase: SendEventUseCase,
    private val getConnectionStatusUseCase: GetConnectionStatusUseCase,
    private val joinGameUseCase: JoinGameUseCase
) : ViewModel() {

    private val _userName = MutableStateFlow("")
    private var _navigationState = MutableStateFlow<NavigationState?>(null)
    val navigationState: StateFlow<NavigationState?> get() = _navigationState

    private var _uiState = MutableStateFlow(StartScreenState())
    val uiState: StateFlow<StartScreenState> get() = _uiState

    val _connectionStatus = MutableStateFlow(false)

    val roomData = MutableStateFlow<RoomEntity>(
        RoomEntity(
            roomId = "", port = "",
            hostName = "", ipHost = "", list = listOf()
        )
    )

    private var gameWebSocketServer: GameWebSocketServer? = null
    private var chosenPort: Int = 0

    fun handleIntent(intent: StartScreenIntent, name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (intent) {
                StartScreenIntent.LanButton -> {
                    _uiState.value = ShowLanDialog
                }

                StartScreenIntent.OnBoxClick -> {
                    _uiState.value = HideLanDialog
                }

                is StartScreenIntent.CreateRoom -> {
                    MangerLanPlayerState.setState(LanPlayerState.Host)
                    _userName.value = name

                    ServerSocket(0).use { serverSocket ->
                        chosenPort = serverSocket.localPort

                        gameWebSocketServer = WebSocketServerManger.createServer(chosenPort)

                        gameWebSocketServer?.start()

                        viewModelScope.launch(Dispatchers.IO) {
                            createRoomUseCase.invoke(
                                player = _userName.value,
                                chosenPort.toString()
                            )

                            gameWebSocketServer!!.addHostPlayer(_userName.value)
                            gameWebSocketServer?.printPlayers()

                            withContext(Dispatchers.Main) {
                                _navigationState.value = NavigationState.GoingToGame
                            }
                        }
                    }
                }

                is StartScreenIntent.Join -> {
                    MangerLanPlayerState.setState(LanPlayerState.Client)
                    launch(Dispatchers.IO) {
                        connectToRoomUseCase.invoke(
                            roomData.value.ipHost,
                            roomData.value.port
                        )

                        handleConnectionStatus()
                    }
                }

                is StartScreenIntent.GoingRoom -> {
                    launch(Dispatchers.IO) {
                        getRoomUseCase.invoke(name).collect { result ->
                            withContext(Dispatchers.Main) {
                                when (result) {
                                    is UiResult.Error -> {
                                        _uiState.value = Error(result.error)
                                    }

                                    UiResult.Loading -> {
                                        _uiState.value = Loading
                                    }

                                    is UiResult.Success -> {
                                        _uiState.value = Success(result.data!!)
                                        _userName.value = name
                                        roomData.value = result.data!!
                                    }

                                    UiResult.TimeOut -> {
                                        _uiState.value =
                                            Timeout("Still no rooms available. Please check back soon!")
                                    }
                                }
                            }
                        }
                    }
                }

                is StartScreenIntent.Back -> {
                    _uiState.value = HideRooms
                }
            }
        }
    }

    private fun sendPlayer() {
        viewModelScope.launch {
            joinGameUseCase.invoke(_userName.value)
        }
    }

    private fun handleConnectionStatus() {
        viewModelScope.launch {
            getConnectionStatusUseCase.invoke().collect { status ->
                _connectionStatus.value = status


                when (status) {
                    true -> {
                        sendPlayer()
                        _navigationState.value = NavigationState.GoingToGame
                    }

                    false -> {
                        _uiState.value = Loading

                        var retryCount = 0
                        val maxRetries = 5

                        while (retryCount < maxRetries && !_connectionStatus.value) {
                            delay(1000 * (retryCount + 1).toLong())

                            if (!_connectionStatus.value) {
                                connectToRoomUseCase.invoke(
                                    roomData.value.ipHost,
                                    port = roomData.value.port
                                )
                                retryCount++
                            }
                        }

                        if (retryCount >= maxRetries && !_connectionStatus.value) {
                            _uiState.value =
                                Error("Failed to establish connection after $maxRetries attempts")
                        }
                    }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        gameWebSocketServer?.stop()
    }
}