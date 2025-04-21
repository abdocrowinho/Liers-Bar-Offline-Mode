package com.example.liersbarofflinemode.ui.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.UseCase.GenerateImageUseCase
import com.example.domain.Validation.UseCase.UserNamesValidationUseCase
import com.example.liersbarofflinemode.ui.Intent.EnterScreenIntent
import com.example.liersbarofflinemode.ui.States.EnterScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EnterUsersScreenViewModel @Inject constructor
    (
    private val userNamesValidationUseCase: UserNamesValidationUseCase,
    private val generateImageUseCase: GenerateImageUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(EnterScreenState())
    val state: StateFlow<EnterScreenState> get() = _state

    fun handleIntent(intent: EnterScreenIntent) {
        when (intent) {
            is EnterScreenIntent.StartIntent ->
                startPlayClicked(
                    user1 = intent.user1,
                    user2 = intent.user2,
                    user3 = intent.user3,
                    user4 = intent.user4,
                )
        }
    }


    private fun startPlayClicked(
        user1: String, user2: String, user3: String, user4: String,
    ) {
        val result = userNamesValidationUseCase.invoke(user1, user2, user3, user4)
        if (result.isNotEmpty()) {
            _state.value = EnterScreenState(players = emptyList() , result )
        } else {
            val listOfNames = listOf(user1, user2, user3, user4)
            generateImagesGame(listOfNames)
        }
    }

    private fun generateImagesGame(listOfNames: List<String>) {
        try {
            viewModelScope.launch {
                val data = generateImageUseCase.invoke(listOfNames)
                _state.value = EnterScreenState(data,null)
                Log.d("players", "generateImagesGame: ${_state.value} ")
            }
        } catch (e: Exception) {
            Log.e("TAG", "generateImagesGame: $e")
        }
    }


}