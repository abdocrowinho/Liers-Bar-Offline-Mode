import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.zIndex
import com.example.liersbarofflinemode.ui.Utltiy.GetHeightConf
import com.example.liersbarofflinemode.ui.Utltiy.GetWidthConf

@Composable
fun ThrowCardsAnimation(
    triggerAnimation: Boolean,
    direction: ThrowDirection = ThrowDirection.BOTTOM
,
            onAnimationEnd: () -> Unit,
) {
    val xOffset = remember { Animatable(0f) }
    val yOffset = remember { Animatable(0f) }

    LaunchedEffect(triggerAnimation) {
        if (triggerAnimation) {
            
            when (direction) {
                ThrowDirection.BOTTOM -> yOffset.snapTo(300f)
                ThrowDirection.TOP -> yOffset.snapTo(-300f)
                ThrowDirection.LEFT-> xOffset.snapTo(-300f)
                ThrowDirection.RIGHT -> xOffset.snapTo(300f)
            }

            xOffset.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing)
            )
            yOffset.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing)
            )

            onAnimationEnd()
        }
    }

    if (triggerAnimation) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(10f),
            contentAlignment = Alignment.Center
        ) {

                Image(
                    painter = painterResource(id = com.example.domain.R.drawable.card_back),
                    contentDescription = "Card",
                    modifier = Modifier
                        .width(GetWidthConf() * .5f)
                        .height(GetHeightConf() * .2f)
                        .graphicsLayer {
                            translationX = xOffset.value
                            translationY = yOffset.value
                        }
                )

        }
    }
}
enum class ThrowDirection{
    BOTTOM,
    RIGHT,
    LEFT,
     TOP,
}