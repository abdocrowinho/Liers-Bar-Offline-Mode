package com.example.domain.Entitys

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserEntity(
    val name: String,
    val image: String,
    var isAlive: Boolean = true,
    var numOfShot: Int,
    var remainingBullets: Int
) : Parcelable {

    fun shoot() {
        if (this.isAlive) {
            if (this.remainingBullets != this.numOfShot) {
                this.remainingBullets--
            } else if (this.remainingBullets==this.numOfShot) {
                this.isAlive = false
                this.remainingBullets = 0
            }
        }else{
            return
        }
    }
}