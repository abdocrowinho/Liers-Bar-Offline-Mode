package com.example.liersbarofflinemode.ui.composable.LanGamePlay.Composable

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.liersbarofflinemode.ui.Intent.EventsLanGamePlayIntent
import com.example.liersbarofflinemode.ui.ViewModels.LanGamePlayViewModel

@Composable
fun WarningBox(isWarning:Boolean){
    if (isWarning) {
        val infiniteTransition = rememberInfiniteTransition(label = "warningTransaction")
        val alpha by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 0.6f,
            animationSpec = infiniteRepeatable(
                animation = tween(500),
                repeatMode = RepeatMode.Reverse
            ), label = "warningInfiniteTransaction"
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Red.copy(alpha = alpha))
        )
    }
}