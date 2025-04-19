package com.example.liersbarofflinemode.ui.Intent

import androidx.lifecycle.ViewModel
import com.example.domain.UseCase.GenerateImageUseCase
import com.example.domain.Validation.UseCase.UserNamesValidationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Singleton

@HiltViewModel
class EnterUsersScreenViewModel @Inject constructor
    (private val userNamesValidationUseCase: UserNamesValidationUseCase,
            private val  generateImageUseCase: GenerateImageUseCase) : ViewModel(){

             suspend  fun handleIntent(intent: EnterScreenIntent){
                    when (intent){
                        is EnterScreenIntent.StartIntent -> startPlayClicked(
                            user1 = intent.user1,
                            user2 = intent.user2,
                            user3 = intent.user3,
                            user4 = intent.user4,
                            onValidatorListener = intent.onValidationListener
                        )
                    }
                }


  suspend  fun startPlayClicked(user1: String, user2: String, user3: String, user4: String,
                         onValidatorListener : (errorField:Int,msg:String)->Unit
                         ) {

        val result = userNamesValidationUseCase.invoke(user1,user2,user3,user4)
        if (!result.isValid&&result.errorField!=null){
            onValidatorListener(result.errorField?:0,
                result.errorMessage?:"UnKnown error")
        }else{
            val listOfNames = listOf(user1,user2,user3,user4)
            goToGame(listOfNames)
        }
    }

    private suspend fun goToGame(listOfNames:List<String>) {
        generateImageUseCase.invoke(listOfNames)
        println(generateImageUseCase.invoke(listOfNames))
    }


}