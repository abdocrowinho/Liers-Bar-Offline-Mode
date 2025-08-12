package com.example.liersbarofflinemode.ui.States

import com.example.domain.Entitys.UserEntity

data class EnterScreenState (
    var players : List<UserEntity> = emptyList(),
    var errors : Map<Int? ,String?>? = null
)

