package com.example.liersbarofflinemode.ui.composable.gamePlayScreen

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.magnifier
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.domain.Entitys.UserEntity
import com.example.liersbarofflinemode.R
import com.example.liersbarofflinemode.ui.Utltiy.ArgumentsKeys
import com.example.liersbarofflinemode.ui.Utltiy.GetHeightConf
import com.example.liersbarofflinemode.ui.Utltiy.GetWidthConf
import com.example.liersbarofflinemode.ui.composable.gamePlayScreen.Composable.PlayerAvatar
import com.example.liersbarofflinemode.ui.theme.red_orange
import com.example.liersbarofflinemode.ui.theme.warm_peach

@Composable
fun GamePlayScreen(navHostController: NavHostController) {
    val players = navHostController.previousBackStackEntry?.savedStateHandle?.get<List<UserEntity>>(
        ArgumentsKeys.PLAYERS_KEY
    )
    val user1 = players!![0]
    val user2 = players[1]
    val user3 = players[2]
    val user4 = players[3]

    println(user1.image)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painter = painterResource(id = R.drawable.game_play_background),
                contentScale = ContentScale.FillBounds
            )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            PlayerAvatar(user2, -75f){user2.shoot()
                Log.d("user2 State", "user2:$user2 ")
            }
            PlayerAvatar(user4,75f){user4.shoot()
                Log.d("user4 State", "user4:$user4 ")

            }
        }
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            PlayerAvatar(user1,-2f){user1.shoot()
                Log.d("user1 State", "user1:$user1")

            }
            PlayerAvatar(user3 , 2f){user3.shoot()
                Log.d("user3 State", "user3:$user3 ")

            }
        }
    }
}

