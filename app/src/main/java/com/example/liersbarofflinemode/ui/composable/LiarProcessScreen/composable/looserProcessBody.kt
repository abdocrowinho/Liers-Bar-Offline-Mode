package com.example.liersbarofflinemode.ui.composable.LiarProcessScreen.composable

import LanPlayerAvatar
import android.media.MediaPlayer
import android.speech.tts.TextToSpeech
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import com.example.domain.Entitys.Card
import com.example.domain.Entitys.LanUserEntity
import com.example.domain.Entitys.Rank
import com.example.liersbarofflinemode.R
import com.example.liersbarofflinemode.ui.States.TablePlayersState
import com.example.liersbarofflinemode.ui.Utltiy.GetHeightConf
import com.example.liersbarofflinemode.ui.Utltiy.GetWidthConf
import com.example.liersbarofflinemode.ui.composable.LiarProcessScreen.helper.AnimationStepper
import com.example.liersbarofflinemode.ui.composable.MultipleGunScreen.composable.FireEffectAnimation
import com.example.liersbarofflinemode.ui.composable.MultipleGunScreen.composable.Gun
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun LooserProcessBody(
    state: TablePlayersState,
    tts: TextToSpeech,
    back: () -> Unit
) {
    val localContext = LocalContext.current
    val playerAvatarOffsetXAnimation = remember { androidx.compose.animation.core.Animatable(-500f) }
    val gunOffsetXAnimation = remember { androidx.compose.animation.core.Animatable(500f) }
    val playerAvatarOffsetX = (GetWidthConf() * .2f.times(-1)).value
    val gunOffsetX = (GetWidthConf() * .2f).value

    var currentStep by remember { mutableStateOf(AnimationStepper.IDLE) }

    // Fix: snapshot bullet count BEFORE animation starts
    // so we can show original count until step 4
    val originalBullets = remember(state.looser?.id) {
        val current = state.looser?.remainingBullets ?: 0
        if (state.isRealBullet == true) {
            current + 1  // restore for display
        } else {
            current + 1  // same either way
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.game_play_background),
            contentDescription = "backGround",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )

        state.looser?.let { looser ->

            // Show original bullets until BULLETS_UPDATE step
            val displayPlayer = remember(currentStep) {
                if (currentStep >= AnimationStepper.BULLETS_UPDATE) {
                    looser // show updated bullets from server
                } else {
                    // show bullets BEFORE the shot
                    looser.copy(remainingBullets = state.bulletsBeforeShot)
                }
            }

            LanPlayerAvatar(
                rotate = 0f,
                playerState = displayPlayer,
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(x = playerAvatarOffsetXAnimation.value.dp),
                size = 80.dp,
            )

            Gun(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(x = gunOffsetXAnimation.value.dp),
                height = GetHeightConf() * .5f,
                width = GetWidthConf() * .3f
            ) {}

            FireEffectAnimation(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(
                        x = GetWidthConf() * .08f.times(-1),
                        y = GetHeightConf() * .12f.times(-1)
                    ),
                isDeadlyBullet = (state.isRealBullet == true) &&
                        currentStep >= AnimationStepper.GUN_FIRING
            )

            AnimatedVisibility(
                visible = currentStep == AnimationStepper.TEXT_SHOWING ||
                        currentStep == AnimationStepper.BULLETS_UPDATE,
                enter = fadeIn(animationSpec = tween(600)),
                exit = fadeOut(animationSpec = tween(600)),
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Text(
                    text = state.bulletWordState ?: "",
                    fontSize = 23.sp,
                    color = Color.White
                )
            }
        }
    }

    LaunchedEffect(key1 = state.looser) {
        if (state.looser == null) return@LaunchedEffect

        // Step 1: slide in avatar + gun
        currentStep = AnimationStepper.SLIDING_IN
        launch {
            playerAvatarOffsetXAnimation.animateTo(
                targetValue = playerAvatarOffsetX,
                animationSpec = tween(1000)
            )
        }
        gunOffsetXAnimation.animateTo(
            targetValue = gunOffsetX,
            animationSpec = tween(1000)
        )

        // Step 2: gun fires
        currentStep = AnimationStepper.GUN_FIRING
        val soundResId = state.gunSound ?: 0
        if (soundResId != 0) {
            val player = MediaPlayer.create(localContext, soundResId)
            player?.start()
            var soundDone = false
            player?.setOnCompletionListener {
                soundDone = true
                it.release()
            }
            var waited = 0
            while (!soundDone && waited < 3000) {
                delay(100)
                waited += 100
            }
        }

        // Step 3: spoken text + TTS
        currentStep = AnimationStepper.TEXT_SHOWING
        tts.speak(
            state.bulletWordState ?: "",
            android.speech.tts.TextToSpeech.QUEUE_FLUSH,
            null,
            null
        )
        delay(2000)

        // Step 4: bullets decrease AFTER gun fires and text shows
        currentStep = AnimationStepper.BULLETS_UPDATE
        delay(1000)

        // Step 5: done
        currentStep = AnimationStepper.DONE
        back()
    }
}