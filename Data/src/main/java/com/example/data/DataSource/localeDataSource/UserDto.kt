package com.example.data.DataSource.localeDataSource

import android.os.Parcelable
import com.example.domain.Entitys.UserEntity

data class UserDto(
    val name: String? = " ",
    val image: String? = " ",
    var isAlive: Boolean = true,
    var numOfShot: Int? = 1,
    var remainingBullets: Int? = 6
) {
    fun userDtoToUserEntity(): UserEntity {
        return UserEntity(
            name, image, isAlive, numOfShot, remainingBullets
        )
    }
}


