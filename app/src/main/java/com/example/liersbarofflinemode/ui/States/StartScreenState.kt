package com.example.liersbarofflinemode.ui.States

import com.example.domain.Entitys.RoomEntity

open class StartScreenState(){}

data object ShowLanDialog : StartScreenState()
data object HideLanDialog : StartScreenState()
data object GetRoom : StartScreenState()
data object HideRooms : StartScreenState()



data object Loading : StartScreenState()
data class Success(val date : RoomEntity) : StartScreenState()
data class Error(val error : String) : StartScreenState()
data class Timeout(val timeOutMessage : String) : StartScreenState()

