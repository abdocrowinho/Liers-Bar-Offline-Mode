package com.example.domain.UseCase

import com.example.domain.Entitys.UserEntity
import kotlinx.coroutines.flow.first
import retrofit2.Response
import javax.inject.Inject

class GenerateImageUseCase  {
    fun invoke(listOfNames :List<String> ):List<UserEntity>{
        val listOfUsers = listOf(
            UserEntity(name = listOfNames[0],
            image = "https://robohash.org/${listOfNames[0]}",
            isAlive = true,
            numOfShot = (1..6).random(),
            remainingBullets = 6
        ),
            UserEntity(name = listOfNames[1],
                image = "https://robohash.org/${listOfNames[1]}",
                isAlive = true,
                numOfShot = (1..6).random(),
                remainingBullets = 6
            ),
            UserEntity(name = listOfNames[2],
                image = "https://robohash.org/${listOfNames[2]}",
                isAlive = true,
                numOfShot = (1..6).random(),
                remainingBullets = 6
            ),
            UserEntity(name = listOfNames[3],
                image = "https://robohash.org/${listOfNames[3]}",
                isAlive = true,
                numOfShot = (1..6).random(),
                remainingBullets = 6
            ),
            )
      return listOfUsers
    }
}