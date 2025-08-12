package com.example.domain.Entitys

import android.os.Parcelable
import android.util.Log
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserEntity(
    val name: String ?=" ",
    val image: String?=" ",
    var isAlive: Boolean = true,
    var numOfShot: Int ?=1,
    var remainingBullets: Int?=6
) : Parcelable
