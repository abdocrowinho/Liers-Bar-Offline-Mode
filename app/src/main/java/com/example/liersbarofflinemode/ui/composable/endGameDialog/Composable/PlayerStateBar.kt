package com.example.liersbarofflinemode.ui.composable.endGameDialog.Composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.domain.Entitys.UserEntity
import com.example.liersbarofflinemode.R
import com.example.liersbarofflinemode.ui.Utltiy.GetHeightConf
import com.example.liersbarofflinemode.ui.Utltiy.GetWidthConf
import com.example.liersbarofflinemode.ui.theme.red_orange

@Composable
fun PlayerStateBar(
    list: List<UserEntity>,
    text: String? = "",
    hasUserName: Boolean? = true
) {
    if (list.isEmpty()) return

    val listReversed = list.reversed()
    listReversed.forEachIndexed { i, user ->
        Row(
            modifier = Modifier
                .padding(vertical = 5.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                Text(
                    text = (i + 1).toString(),
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(end = 8.dp)
                )
                AsyncImage(
                    model = user.image,
                    contentDescription = "userimage$i",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .size(55.dp)
                        .border(3.dp, red_orange, shape = RoundedCornerShape(15.dp))
                )
                Text(
                    text = when (hasUserName) {
                        true -> user.name.toString()
                        false -> ""
                        null -> user.name.toString()
                    },
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(start = 8.dp)
                )
                Text(
                    text = text ?: "",
                    modifier = Modifier.align(Alignment.CenterVertically),
                    color = Color.White
                )
            }

            // Fix 3: guard against null/zero numOfShot
            val shots = (user.numOfShot ?: 0).coerceAtLeast(0)
            if (shots > 0) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    for (j in 1..shots) {
                        Image(
                            painter = painterResource(id = R.drawable.bullet_top_destention),
                            contentDescription = "deadlyBullet$j",
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier
                                .width(GetWidthConf() * .015f)
                                .height(GetHeightConf() * .08f)
                        )
                        Spacer(modifier = Modifier.width(GetWidthConf() * .005f))
                    }
                }
            }
        }
    }
}