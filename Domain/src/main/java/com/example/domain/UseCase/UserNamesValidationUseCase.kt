package com.example.domain.Validation.UseCase

class UserNamesValidationUseCase() {

    operator fun invoke(
        userName1: String, userName2: String, userName3: String, userName4: String
    ): Map<Int?, String?> {
        val fields = listOf(userName1, userName2, userName3, userName4)
        val errors = mutableMapOf<Int?, String?>()

        fields.forEachIndexed { index, userName ->

            if (userName.isBlank()) {
                errors[index] = "field is required"
            } else {
                if (userName.length < 4) {
                    errors[index] = "field cant be less\n" +
                            " than 4 letters"
                }
            }
        }
        return errors
    }
}
