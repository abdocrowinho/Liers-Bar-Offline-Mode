package com.example.liersbarofflinemode.ui.composable.startScreen.Composable

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

@Composable
fun CustomLoadingIndicator(modifier: Modifier) {
    val infiniteTransition = rememberInfiniteTransition("")

    val rotation1 by infiniteTransition.animateFloat(
        label = "",
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(animation = tween(3000, easing = LinearEasing))
    )

    val rotation2 by infiniteTransition.animateFloat(
        label = "",
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(animation = tween(2000, easing = LinearEasing))
    )

    val rotation3 by infiniteTransition.animateFloat(
        label = "",
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(animation = tween(1000, easing = LinearEasing))
    )
    Box(contentAlignment = Alignment.Center, modifier = modifier.size(100.dp)) {

        CircularProgressIndicator(modifier = Modifier
            .graphicsLayer { rotationZ = rotation1 }
            .size(90.dp)
            .padding(5.dp), strokeWidth = 5.dp)
        CircularProgressIndicator(modifier = Modifier
            .graphicsLayer { rotationZ = rotation2 }
            .size(60.dp)
            .padding(10.dp), strokeWidth = 5.dp)
        CircularProgressIndicator(modifier = Modifier
            .graphicsLayer { rotationZ = rotation3 }
            .size(30.dp)
            .padding(15.dp), strokeWidth = 5.dp)
    }
}