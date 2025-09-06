package com.example.data.DataSource.localeDataSource.RepoImpl

import UDPListener
import android.content.Context
import android.util.Log
import com.example.data.DataSource.localeDataSource.LanServeis.GamePlayLanWebSocketFactory
import com.example.data.DataSource.localeDataSource.LanServeis.GamePlayWepSocketFactoryImpl
import com.example.data.DataSource.localeDataSource.LanServeis.GameWebSocketClient
import com.example.data.DataSource.localeDataSource.UDPs.UDPBroadcaster
import com.example.domain.Entitys.LanUserEntity
import com.example.domain.Entitys.RoomEntity
import com.example.domain.Entitys.UserEntity
import com.example.domain.GameEvents.Event
import com.example.domain.GameEvents.JoinToGameEvent
import com.example.domain.Repo.LanGamePLay
import com.example.domain.Utlites.UiResult
import com.example.domain.Utlites.getMyIpAddress
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.net.URI
import javax.inject.Inject
import javax.inject.Scope

@Suppress("UNREACHABLE_CODE")
class LanGamePlayRepoImpl @Inject constructor(
  private  val gamePlayLanWebSocketFactory: GamePlayLanWebSocketFactory,

    @ApplicationContext val context: Context
) : LanGamePLay {
    private lateinit var webSocketClient: GameWebSocketClient

private val _isWepSocketOpen = MutableStateFlow(false)
    override fun isWebSocketOpen(): Flow<Boolean> =_isWepSocketOpen
    override suspend fun createRoom(room: RoomEntity) {
        UDPBroadcaster.startBroadcasting(room)

    }
  override suspend fun connectToGameServer(serverIp: String,port:String ) {
        val uri = URI("ws://$serverIp:$port/game")
        webSocketClient = gamePlayLanWebSocketFactory.create(
             uri, onSuccess = {
                 _isWepSocketOpen.value = true

            }, onErrorAction = {
                _isWepSocketOpen.value=false
            })
        webSocketClient.connect()
    }

    override suspend fun join(playerName: String) {
        val newUser = LanUserEntity(
            name = playerName,
            numOfShot = (1..6).random(),
            remainingBullets = 6,
            isAlive = true,
            image = "https://robohash.org/${playerName}?set=set5",
            id = 0,
            ipAddress = "",
            cards = mutableListOf()
        )
        webSocketClient.sendGameEvent(event = JoinToGameEvent(newUser))
    }



    override fun getRoom(): Flow<UiResult<RoomEntity>> = callbackFlow {
        trySend(UiResult.Loading)

        val receiver = UDPListener(context = context) { roomEntity ->
            trySend(UiResult.Success(roomEntity))
            close()
        }
        receiver.startListening()

        val timeout = launch {
            delay(5000)
            trySend(UiResult.Error("There are no empty rooms available at the moment."))
            close()
        }
        awaitClose {
            receiver.stop()
            timeout.cancel()
        }
    }.flowOn(Dispatchers.IO)

    override fun sendEvent(event: Event) {
        webSocketClient.sendGameEvent(event)
    }

    override fun getLanPlayers(): MutableStateFlow<MutableList<LanUserEntity?>?> {
      return  webSocketClient.playersInRoom
    }

    override suspend fun getMessage():SharedFlow<Event?> {
        return webSocketClient.messageEvent.also {
            flow->
            CoroutineScope(Dispatchers.IO).launch {
                flow.collect{event->
                    Log.d("REPO", "received event in repo -> $event")
                }
            }
        }
    }

}