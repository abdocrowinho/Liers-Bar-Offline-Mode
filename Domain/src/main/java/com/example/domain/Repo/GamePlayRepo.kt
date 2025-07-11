package com.example.domain.Repo

import com.example.domain.Entitys.UserEntity
import kotlinx.coroutines.flow.MutableStateFlow

interface GamePlayRepo {
     fun generateMultipleGame(playersNames : List<String>) : List<UserEntity>
     fun setPlayers(playersList : List<UserEntity>)
     fun getPlayersState():Map<Int, MutableStateFlow<UserEntity?>>
     fun killGame()


}