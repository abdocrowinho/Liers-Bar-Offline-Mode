package com.example.liersbarofflinemode.ui.sharedViewModels

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import com.example.domain.Entitys.UserEntity
import kotlinx.coroutines.flow.MutableStateFlow

class SharedViewModelBetweenEnterUserScreenAndGamePlayScreen : ViewModel() {
  private  var _gamePlayArgumentsState = MutableStateFlow<List <UserEntity>?>(null)

    fun sendArgumentWithNavigation(players:List<UserEntity>?){
      _gamePlayArgumentsState.value=players
    }
}