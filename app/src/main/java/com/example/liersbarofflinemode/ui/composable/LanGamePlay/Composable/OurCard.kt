package com.example.liersbarofflinemode.ui.composable.LanGamePlay.Composable

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleCoroutineScope
import com.example.domain.Entitys.Card
import com.example.liersbarofflinemode.ui.Intent.LanGamePlayIntent
import com.example.liersbarofflinemode.ui.Utltiy.GetHeightConf
import com.example.liersbarofflinemode.ui.Utltiy.GetWidthConf
import com.example.liersbarofflinemode.ui.ViewModels.LanGamePlayViewModel
import com.example.liersbarofflinemode.ui.theme.green_color
import com.example.liersbarofflinemode.ui.theme.red_orange
import kotlinx.coroutines.launch
import org.jetbrains.annotations.Async


@Composable
fun OurCard(card: Card, viewModel: LanGamePlayViewModel) {
    val readyCardState by viewModel.readyCard.collectAsState()
    val offsetY = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()
    val minOffset = -107f
    val maxOffset = 150f
    val isSelected = readyCardState.contains(card)

    // Guard: skip rendering if imageCard is invalid
    if (card.imageCard == 0) return

    Box(
        modifier = Modifier
            .offset { IntOffset(x = 0, offsetY.value.toInt()) }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        coroutineScope.launch {
                            if (offsetY.value == minOffset) {
                                offsetY.animateTo(minOffset)
                            } else {
                                offsetY.animateTo(0f)
                            }
                        }
                        if (offsetY.value == minOffset) {
                            viewModel.handelIntentCards(LanGamePlayIntent.AddReadyCard(card))
                        } else {
                            viewModel.handelIntentCards(LanGamePlayIntent.RemoveReadyCard(card))
                        }
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        val newOffset = (offsetY.value + dragAmount.y).coerceIn(
                            minimumValue = minOffset,
                            maximumValue = maxOffset
                        )
                        if (readyCardState.size == 3 && !readyCardState.contains(card)) return@detectDragGestures
                        coroutineScope.launch {
                            offsetY.snapTo(newOffset)
                        }
                    }
                )
            }
    ) {
        Image(
            painter = painterResource(id = card.imageCard),
            contentScale = ContentScale.FillBounds,
            contentDescription = "card",
            modifier = Modifier
                .width(GetWidthConf() * .07f)
                .height(GetHeightConf() * .25f)
                .border(
                    width = if (isSelected) 2.dp else 0.dp,
                    color = if (isSelected) green_color else Color.Transparent,
                    shape = RoundedCornerShape(4.dp)
                )
        )
    }
}

