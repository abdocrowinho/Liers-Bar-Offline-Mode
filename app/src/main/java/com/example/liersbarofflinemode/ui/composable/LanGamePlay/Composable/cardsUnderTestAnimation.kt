package com.example.liersbarofflinemode.ui.composable.LanGamePlay.Composable

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.domain.Entitys.Card
import com.example.liersbarofflinemode.ui.Utltiy.GetHeightConf
import com.example.liersbarofflinemode.ui.Utltiy.GetWidthConf
import com.example.liersbarofflinemode.ui.theme.mainActivity.ui.Bases.NavigationItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CardsUnderTestAnimation(
    cardsUnderTest: List<Card?>?,
    navController: NavController,
    isPlayerCallLiar: Boolean?,
) {
    val screenHeight = GetHeightConf().value
    val screenWidth = GetWidthConf().value

    val yStart = -screenHeight * 2f
    val yTarget = -1f
    val cardSpacing = screenWidth * 0.1f

    // نحتفظ بـ Animatable لكل كارت
    val yOffsets = remember(cardsUnderTest?.size) {
        cardsUnderTest?.map { Animatable(yStart) } ?: emptyList()
    }

    // عرض الكروت في منتصف الشاشة
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        cardsUnderTest?.forEachIndexed { index, card ->
            val yOffset = yOffsets.getOrNull(index)
            val xOffset = (index - (cardsUnderTest.lastIndex / 2f)) * cardSpacing
            if (card != null && yOffset != null) {
                SingleCard(
                    card = card,
                    modifier = Modifier.offset(
                        x = xOffset.dp,
                        y = (yOffset.value / (screenHeight / 100f)).dp
                    )
                )
            }
        }
    }

    // 🔥 هنا بقى اللي بيشغل الأنيميشن فعليًا
    LaunchedEffect(isPlayerCallLiar, cardsUnderTest) {
        println("🔥 LaunchedEffect triggered, liar=$isPlayerCallLiar, cards=${cardsUnderTest?.size}")
        if (isPlayerCallLiar == true && !cardsUnderTest.isNullOrEmpty()) {
            println("🎬 Animation started!")
            yOffsets.forEachIndexed { index, anim ->
                launch {
                    delay(index * 300L)
                    anim.animateTo(
                        targetValue = yTarget,
                        animationSpec = tween(1200)
                    )
                }
            }
            delay(2500)
            navController.navigate(NavigationItem.LiarProcessScreen.route)
        }
    }
}