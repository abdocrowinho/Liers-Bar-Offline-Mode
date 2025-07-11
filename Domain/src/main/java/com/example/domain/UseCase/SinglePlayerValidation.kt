package com.example.domain.UseCase

class SinglePlayerValidation {
    fun invoke(playerName: String): String {

        val error = if (playerName.isBlank()) {
            "field can't be empty"

        } else if (playerName.length < 4) {
            "field can't be less \n than 4"
        } else {
            ""
        }
        return error
    }
}