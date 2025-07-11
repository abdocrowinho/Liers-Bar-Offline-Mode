package com.example.data.DataSource.localeDataSource.RepoImpl

import com.example.domain.Entitys.UserEntity
import com.example.domain.Repo.GamePlayRepo
import com.example.domain.Repo.SinglePlayerRepo
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Singleton

@Singleton
class SinglePlayRepoImpl : SinglePlayerRepo {
    private val _singlePlayerState = MutableStateFlow(UserEntity())
     override fun generateSingleGame(playerName: String) {
          _singlePlayerState.value = UserEntity(name = playerName , remainingBullets = 6,
               numOfShot = (1..6).random(), isAlive = true ,
               image = "https://robohash.org/$playerName"
               )
     }

     override fun getPlayerState(): MutableStateFlow<UserEntity> {
      return    _singlePlayerState
     }


}