package com.example.liersbarofflinemode.ui.States

sealed class NavigationState {
    data object GoingToGame : NavigationState()
    class ShowError(val error: String) : NavigationState()
     data object Loading:NavigationState()
}
