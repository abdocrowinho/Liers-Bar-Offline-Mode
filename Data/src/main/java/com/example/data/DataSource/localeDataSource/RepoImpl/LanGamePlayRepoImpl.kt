package com.example.data.DataSource.localeDataSource.RepoImpl

import UDPListener
import android.content.Context
import com.example.data.DataSource.localeDataSource.LanServeis.GamePlayLanWebSocketFactory
import com.example.data.DataSource.localeDataSource.LanServeis.GamePlayWepSocketFactoryImpl
import com.example.data.DataSource.localeDataSource.LanServeis.GameWebSocketClient
import com.example.data.DataSource.localeDataSource.UDPs.UDPBroadcaster
import com.example.domain.Entitys.LanUserEntity
import com.example.domain.Entitys.RoomEntity
import com.example.domain.Entitys.UserEntity
import com.example.domain.GameEvents.Event
import com.example.domain.Repo.LanGamePLay
import com.example.domain.Utlites.UiResult
import com.example.domain.Utlites.getMyIpAddress
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.net.URI
import javax.inject.Inject

class LanGamePlayRepoImpl @Inject constructor(
  private  val gamePlayLanWebSocketFactory: GamePlayLanWebSocketFactory,
    @ApplicationContext val context: Context
) : LanGamePLay {
    private lateinit var webSocketClient: GameWebSocketClient
    override suspend fun createRoom(room: RoomEntity) {
        UDPBroadcaster.startBroadcasting(room)

    }
  override suspend fun connectToGameServer(serverIp: String,

                                           onConnectedError:(String) -> Unit, onConnectedSuccess:()->Unit) {
        val uri = URI("ws://$serverIp:8080/game")
        webSocketClient = gamePlayLanWebSocketFactory.create(
             uri,
            onSuccess ={
                onConnectedSuccess()
            },
            onErrorAction ={error->
                onConnectedError(error)
            }
        )
        webSocketClient.connect()
    }




    override suspend fun join(playerName: String) {
        val newUser = UserEntity(
            name = playerName,
            numOfShot = (1..6).random(),
            remainingBullets = 6,
            isAlive = true,
            image = "https://robohash.org/${playerName}?set=set5"
        )
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

    override fun getLanPlayers(): MutableStateFlow<List<LanUserEntity?>?> {
      return  webSocketClient.playersInRoom
    }
}