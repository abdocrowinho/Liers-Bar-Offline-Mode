package com.example.liersbarofflinemode.ui.States

import com.example.domain.Entitys.UserEntity
import com.example.liersbarofflinemode.R
 sealed class GunScreenState

data class GunShotState(
    var userAfterShot: UserEntity? = null,
    var bulletVoice: Int? = null,
    var moveFireBullet: Boolean? = false,
    val bulletStateWord: String ?= ""
): GunScreenState()



data class LastBulletInGame(val playerWinner:List<UserEntity> ) : GunScreenState()