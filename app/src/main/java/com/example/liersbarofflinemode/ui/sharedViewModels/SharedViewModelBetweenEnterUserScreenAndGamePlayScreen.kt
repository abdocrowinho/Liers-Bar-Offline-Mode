package com.example.liersbarofflinemode.ui.sharedViewModels

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class BaseViewModel : ViewModel() {
  private  var _gamePlayArgumentsState = MutableStateFlow(null)

    fun sendArgumentWithNavigation(argument : List<Nothing>,composable: Composable){
    }
}