package com.example.liersbarofflinemode.ui.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.DataSource.localeDataSource.LanServeis.GameWebSocketServer
import com.example.domain.Entitys.LanUserEntity
import com.example.domain.GameEvents.JoinToGameEvent
import com.example.domain.UseCase.ConnectToRoomUseCase
import com.example.domain.UseCase.GetConnectionStatusUseCase
import com.example.domain.UseCase.GetRoomUseCase
import com.example.domain.UseCase.JoinGameUseCase
import com.example.domain.UseCase.SendEventUseCase
import com.example.domain.UseCase.StartRoomUseCase
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
import java.net.ServerSocket
import javax.inject.Inject

@HiltViewModel
class StartScreenViewModel @Inject constructor(
    private val createRoomUseCase: StartRoomUseCase,
    private val getRoomUseCase: GetRoomUseCase,
    private val connectToRoomUseCase: ConnectToRoomUseCase,
    private val sendEventUseCase: SendEventUseCase,
    private val getConnectionStatusUseCase: GetConnectionStatusUseCase
    ,private val joinGameUseCase: JoinGameUseCase


) : ViewModel() {

private val _userName = MutableStateFlow("")

    private var _navigationState = MutableStateFlow<NavigationState?>(null)
    val navigationState: StateFlow<NavigationState?> get() = _navigationState

    private var _uiState = MutableStateFlow(StartScreenState())
    val uiState: StateFlow<StartScreenState> get() = _uiState

    val _connectionStatus = MutableStateFlow(false)
    private val intent = MutableStateFlow<StartScreenIntent?>(null)

    val serverSocket = ServerSocket(0)
    val chosenPort = serverSocket.localPort
    fun handleIntent(intent: StartScreenIntent, name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (intent) {
                StartScreenIntent.LanButton -> _uiState.value = ShowLanDialog

                StartScreenIntent.OnBoxClick -> _uiState.value = HideLanDialog

                is StartScreenIntent.CreateRoom -> {

                    _userName.value = name
                    val gameWebSocketServer = GameWebSocketServer(chosenPort)
                    gameWebSocketServer.setOnStartedListener {
                        println("Server started successfully")

                        viewModelScope.launch {
                            createRoomUseCase.invoke(player = _userName.value,chosenPort.toString())
                            connectToRoomUseCase.invoke(getMyIpAddress(), port = chosenPort.toString())
                           joinGameUseCase.invoke(_userName.value)
                            handleConnectionStatus()
                        }
                    }
                    gameWebSocketServer.start()
                    serverSocket.close()

                }

    is StartScreenIntent.Join -> {
        viewModelScope.launch (Dispatchers.IO){
             val ipHost = (_uiState.value as Success).date.ipHost

            connectToRoomUseCase.invoke(ipHost,chosenPort.toString())
            handleConnectionStatus()

        }
            }
                is StartScreenIntent.GoingRoom -> {

                    viewModelScope.launch(Dispatchers.IO) {
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

                is StartScreenIntent.Back -> _uiState.value = HideRooms
            }
        }

    }



    private fun sendPlayer(){

    val event = JoinToGameEvent( LanUserEntity(
        name = _userName.value , id = 0 , image = "https://robohash.org/${_userName.value}", numOfShot = (1..6).random(), remainingBullets = 6,
        isAlive = true , ipAddress = getMyIpAddress() ,cards = mutableListOf()  )
    )
    sendEventUseCase.invoke(event)

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
                     viewModelScope.launch {
                         delay(500)

                         connectToRoomUseCase.invoke(getMyIpAddress(), port = chosenPort.toString())
                     }
                 }
             }
         }
     }
 }
}