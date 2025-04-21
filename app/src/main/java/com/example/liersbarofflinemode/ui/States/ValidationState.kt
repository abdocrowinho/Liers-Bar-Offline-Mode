package com.example.liersbarofflinemode.ui.States

import com.example.domain.Entitys.UserEntity

data class EnterScreenState (
    val players : List<UserEntity> = emptyList(),
    val errors : Map<Int? ,String?>? = null
)

