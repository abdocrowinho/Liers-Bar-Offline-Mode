package com.example.liersbarofflinemode.ui.composable.MultipleGunScreen.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.airbnb.lottie.RenderMode
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.liersbarofflinemode.R

@Composable
fun FireEffectAnimation(modifier: Modifier , isDeadlyBullet : Boolean){
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.fire_effect))
    val animationState =
        animateLottieCompositionAsState(
            composition = composition,
            isPlaying = isDeadlyBullet,
            speed = .5f,

        )
    LottieAnimation(composition = composition ,
        progress = {animationState.progress}
    , modifier = modifier,
        renderMode = RenderMode.HARDWARE
    )
}
@Preview(device = "spec:parent=pixel_5,orientation=landscape", showSystemUi = true,
    showBackground = true
)
@Composable
fun TestFire() {
    FireEffectAnimation(isDeadlyBullet = true, modifier = Modifier)
}