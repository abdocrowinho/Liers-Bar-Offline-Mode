package com.example.liersbarofflinemode.ui.composable.MultipleGunScreen.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.liersbarofflinemode.R

@Composable
fun FireEffectAnimation(modifier: Modifier , isDeadlyBullet : Boolean){
    val fireEffectAnimation by rememberLottieComposition(spec =
    LottieCompositionSpec.RawRes(R.raw.fire_effect)
    )
    LottieAnimation(composition = fireEffectAnimation
    , modifier = modifier
        , isPlaying = isDeadlyBullet
    )
}