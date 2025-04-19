package com.example.liersbarofflinemode.ui.composable.PlayersNameScreen.Composable

import android.widget.ImageButton
import androidx.annotation.ColorRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.liersbarofflinemode.R
import com.example.liersbarofflinemode.ui.Utltiy.CustomOutLineBorder
import com.example.liersbarofflinemode.ui.Utltiy.GetHeightConf
import com.example.liersbarofflinemode.ui.Utltiy.GetWidthConf
import com.example.liersbarofflinemode.ui.theme.dark_red
import com.example.liersbarofflinemode.ui.theme.mainActivity.ui.Bases.Screens.MainActivity.Composable.Button
import com.example.liersbarofflinemode.ui.theme.red_orange
import com.example.liersbarofflinemode.ui.theme.warm_peach

@Composable
fun EnterUsersScreen(modifier: Modifier) {

    Box(
        modifier = modifier
            .fillMaxSize()
            .paint(
                painterResource(id = R.drawable.multiple_players_screen),
                contentScale = ContentScale.Crop
            )
    ) {
        Column(horizontalAlignment =Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxSize()
                .offset(x = GetWidthConf() * .025f)) {
            Spacer(modifier = Modifier.height(GetHeightConf()*.3f))
            
            Text(text = "enter your names", color = red_orange, fontWeight = FontWeight(weight = 1000))
            Spacer(modifier = Modifier.height(GetHeightConf()*.12f))

            Row {
               CustomOutLineBorder(width = GetWidthConf()*0.16f, radius = 16.dp, color = red_orange )
                Spacer(modifier = modifier.width(GetWidthConf()*.02f))
                CustomOutLineBorder(width = GetWidthConf()*0.16f, radius = 16.dp, color = red_orange )
                Spacer(modifier = modifier.width(GetWidthConf()*.02f))

                CustomOutLineBorder(width = GetWidthConf()*0.16f, radius = 16.dp, color = red_orange )
                Spacer(modifier = modifier.width(GetWidthConf()*.02f))

                CustomOutLineBorder(width = GetWidthConf()*0.16f, radius = 16.dp, color = red_orange )

            }
            Spacer(modifier = modifier.height(GetHeightConf() *.09f))
            Row(horizontalArrangement = Arrangement.Center
            , verticalAlignment = Alignment.Top
            ) {
                Button(onClick = { /*TODO*/ }, text ="Start",height = .08f, width = .12f
                )
                Spacer(modifier = modifier.width(GetWidthConf()*.41f))
                Button(onClick = { /*TODO*/ }, text ="Back",height = .08f, width = .12f )
                Spacer(modifier = modifier.width(GetWidthConf()*.01f))


            }

        }
    }

}


