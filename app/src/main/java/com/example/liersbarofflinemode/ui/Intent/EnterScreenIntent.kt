package com.example.liersbarofflinemode.ui.Intent


sealed class EnterScreenIntent {
    data class StartIntent(
        val user1: String,
        val user2: String,
        val user3: String,
        val user4: String,
        val onValidationListener: (fieldError: Int, msg: String) -> Unit
    ) : EnterScreenIntent()

}