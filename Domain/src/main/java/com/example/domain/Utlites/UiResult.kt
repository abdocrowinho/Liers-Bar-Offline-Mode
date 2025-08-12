package com.example.domain.Utlites

sealed class UiResult<out T> {
    data object Loading : UiResult<Nothing>()
    data class Success<T>(val data: T) : UiResult<T>()
    data class Error<T>(val error: String) : UiResult<T>()
    data object TimeOut : UiResult<Nothing>()

}