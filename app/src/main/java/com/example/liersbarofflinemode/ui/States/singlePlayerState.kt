package com.example.liersbarofflinemode.ui.States

import com.example.domain.Entitys.UserEntity

data class SinglePlayerState (
    var  player : UserEntity ?=null ,
    var error : String?=null
)