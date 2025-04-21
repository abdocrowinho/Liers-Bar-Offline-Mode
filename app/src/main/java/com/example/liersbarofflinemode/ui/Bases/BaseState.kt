package com.example.liersbarofflinemode.ui.Bases

import java.lang.Error

sealed class BaseState <out T>{
    data object Default : BaseState<Nothing>()
    data object Loading : BaseState<Nothing>()
    data class Success<T>(val data:T) : BaseState<T>()
    data class Error(val error:String): BaseState<Nothing>()
}

