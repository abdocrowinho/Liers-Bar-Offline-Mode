package com.example.liersbarofflinemode.ui.ViewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.DataSource.localeDataSource.LanServeis.GameWebSocketServer
import com.example.data.DataSource.localeDataSource.LanServeis.WebSocketServerManger
import com.example.data.DataSource.localeDataSource.UDPs.UDPBroadcaster
import com.example.data.DataSource.localeDataSource.deviceIdManger.DeviceIdManager
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
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.ServerSocket
import javax.inject.Inject

@HiltViewModel
class StartScreenViewModel @Inject constructor(
    private val createRoomUseCase: StartRoomUseCase,
    private val getRoomUseCase: GetRoomUseCase,
    private val connectToRoomUseCase: ConnectToRoomUseCase,
    private val sendEventUseCase: SendEventUseCase,
    private val getConnectionStatusUseCase: GetConnectionStatusUseCase,
    private val joinGameUseCase: JoinGameUseCase,
    @param:ApplicationContext val context: Context
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

    // ✅ Navigation guard — prevents GoingToGame from firing more than once
    private var hasNavigated = false

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

                    val freePort = ServerSocket(0).use { it.localPort }
                    gameWebSocketServer = WebSocketServerManger.createServer(freePort)

                    gameWebSocketServer?.setOnStartedListener {
                        viewModelScope.launch(Dispatchers.IO) {
                            gameWebSocketServer!!.addHostPlayer(_userName.value)
                            gameWebSocketServer?.printPlayers()

                            createRoomUseCase.invoke(
                                player = _userName.value,
                                freePort.toString()
                            )

                            // ✅ Guard for host navigation too
                            withContext(Dispatchers.Main) {
                                if (!hasNavigated) {
                                    hasNavigated = true
                                    _navigationState.value = NavigationState.GoingToGame
                                }
                            }
                        }
                    }

                    gameWebSocketServer?.start()
                }

                is StartScreenIntent.Join -> {
                    MangerLanPlayerState.setState(LanPlayerState.Client)
                    val deviceId = DeviceIdManager.getDeviceId(context)
                    Log.d("Join", "attempting join ip=${roomData.value.ipHost} port=${roomData.value.port} name=${_userName.value} deviceId=$deviceId")
                    launch(Dispatchers.IO) {
                        connectToRoomUseCase.invoke(
                            roomData.value.ipHost,
                            roomData.value.port,
                            deviceId = deviceId,
                            playerName = _userName.value
                        )
                        Log.d("Join", "connectToRoomUseCase done — waiting for status")
                        handleConnectionStatus(deviceId = deviceId, _userName.value)
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

    // ✅ Called from UI after navigation is consumed, so state doesn't re-trigger on recomposition
    fun onNavigationHandled() {
        _navigationState.value = null
    }

    private fun sendPlayer(deviceId: String) {
        viewModelScope.launch {
            joinGameUseCase.invoke(_userName.value, deviceId = deviceId)
        }
    }

    private fun handleConnectionStatus(deviceId: String, playerName: String) {
        viewModelScope.launch {
            Log.d("Join", "handleConnectionStatus started")
            getConnectionStatusUseCase.invoke().collect { status ->
                Log.d("Join", "status=$status hasNavigated=$hasNavigated")
                if (status && !hasNavigated) {
                    hasNavigated = true
                    withContext(Dispatchers.Main) {
                        _navigationState.value = NavigationState.GoingToGame
                    }
                    // Send join event after navigation — give ViewModel time to init
                    delay(800)
                    sendPlayer(deviceId = deviceId)
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        UDPBroadcaster.stopBroadcasting()
        gameWebSocketServer?.stop()
    }
}