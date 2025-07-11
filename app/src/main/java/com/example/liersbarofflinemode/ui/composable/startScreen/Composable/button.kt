package com.example.liersbarofflinemode.ui.theme.mainActivity.ui.Bases.Screens.MainActivity.Composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.liersbarofflinemode.R
import com.example.liersbarofflinemode.ui.Utltiy.GetHeightConf
import com.example.liersbarofflinemode.ui.Utltiy.GetWidthConf
import com.example.liersbarofflinemode.ui.Utltiy.SoundPlayer

@Composable
fun CustomWoodButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
width : Float
    ,height:Float
)

{
    val context = LocalContext.current
    val soundPlayer = remember {
        SoundPlayer(context)
    }
    DisposableEffect(Unit) {
        onDispose {
            soundPlayer.release()
        }
    }
    Box(
        modifier = modifier
            .width(GetWidthConf() * width)
            .height(GetHeightConf() * height)
            .clickable {
                soundPlayer.playClickSound()
                onClick() },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.main_button), contentDescription = "asdf",
            contentScale = ContentScale.FillBounds,
            modifier = modifier.matchParentSize()
        )

        Text(text = text
        ,modifier.align(Alignment.Center)
        )

    }


}