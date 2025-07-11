package com.example.data.DataSource.localeDataSource

import android.util.Log
import com.example.domain.Entitys.UserEntity
import com.example.domain.Repo.GamePlayRepo
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Singleton

@Singleton
class GamePlayRepoImpl : GamePlayRepo {
     private var _playersState = (1..4).associateWith { MutableStateFlow<UserEntity?>(null) }

     override fun generateImageRepo(playersNames : List<String>): List<UserEntity> {
          val listOfUsers = listOf(
               UserEntity(name = playersNames[0],
                    image = "https://robohash.org/${playersNames[0]}",
                    isAlive = true,
                    numOfShot = (1..6).random(),
                    remainingBullets = 6
               ),
               UserEntity(name = playersNames[1],
                    image = "https://robohash.org/${playersNames[1]}",
                    isAlive = true,
                    numOfShot = (1..6).random(),
                    remainingBullets = 6
               ),
               UserEntity(name = playersNames[2],
                    image = "https://robohash.org/${playersNames[2]}",
                    isAlive = true,
                    numOfShot = (1..6).random(),
                    remainingBullets = 6
               ),
               UserEntity(name = playersNames[3],
                    image = "https://robohash.org/${playersNames[3]}",
                    isAlive = true,
                    numOfShot = (1..6).random(),
                    remainingBullets = 6
               ),
          )
          return listOfUsers
     }

     override fun setPlayers(playersList: List<UserEntity>) {
          playersList.forEachIndexed { index, player ->
               _playersState[index + 1]?.value = player
          }

     }

     override fun getPlayersState(): Map<Int, MutableStateFlow<UserEntity?>> {
          return _playersState
     }



     override fun killGame() {
          _playersState.forEach {(_ , player )->
               player.value = null
          }
     }
}

