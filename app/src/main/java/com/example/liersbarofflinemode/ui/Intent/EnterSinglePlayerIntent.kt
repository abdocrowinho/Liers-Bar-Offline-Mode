package com.example.liersbarofflinemode.ui.Intent

import com.example.domain.Entitys.UserEntity

sealed class EnterSinglePlayerIntent {
    data class DoneButton(val userName: String ,
        ) : EnterSinglePlayerIntent()

    data object GunFire : EnterSinglePlayerIntent()
}