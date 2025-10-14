package com.example.liersbarofflinemode.ui.composable.LiarProcessScreen.composable

import LanPlayerAvatar
import android.media.MediaPlayer
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
import com.example.liersbarofflinemode.ui.composable.MultipleGunScreen.composable.FireEffectAnimation
import com.example.liersbarofflinemode.ui.composable.MultipleGunScreen.composable.Gun
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun LooserProcessBody(state: TablePlayersState,startNewRound:suspend()->Unit,back : ()->Unit){
    val localContext = LocalContext.current
    val playerAvatarOffsetXAnimation = remember { androidx.compose.animation.core.Animatable(-500f) }
    val gunOffsetXAnimation = remember { androidx.compose.animation.core.Animatable(500f) }
    val playerAvatarOffsetX =(GetWidthConf() * .2f.times(-1)).value
    val gunOffsetX =(GetWidthConf() * .2f).value
    var bulletWordState by remember { mutableStateOf(false) }
    var isBulletRun by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = R.drawable.game_play_background),
            contentDescription = "backGround",
            contentScale = ContentScale.FillBounds, modifier = Modifier.fillMaxSize()
        )


        state.looser?.let {
            LanPlayerAvatar(
                rotate = 0f, playerState = it,

                modifier = Modifier
                    .align(Alignment.Center)

                    .offset(
                        x = playerAvatarOffsetXAnimation.value.dp
                    ), size = 80.dp
            )


            Gun(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(
                        x = gunOffsetXAnimation.value.dp
                    ),

                height = GetHeightConf() * .5f,
                width = GetWidthConf() * .3f){}

            FireEffectAnimation(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(
                        x = GetWidthConf() * .08f.times(-1),
                        y = GetHeightConf() * .12f.times(-1)
                    )
                   , isDeadlyBullet = state.isRealBullet?:false&&isBulletRun
            )

            AnimatedVisibility(
                visible = bulletWordState,
                enter = fadeIn(
                ),
                exit = fadeOut(animationSpec = tween(1400)),
                modifier = Modifier.align(Alignment.BottomCenter),
            ) {
                Text(
                    text = state.bulletWordState?:"",
                    fontSize = 23.sp,
                    color = Color.White,

                    )
            }

        }

    }

LaunchedEffect(key1 = state.looser) {
    if (state.looser!=null){

            playerAvatarOffsetXAnimation.animateTo(
                targetValue = playerAvatarOffsetX
                , animationSpec = tween(1000)
            )
            gunOffsetXAnimation.animateTo(
                targetValue = gunOffsetX,
                animationSpec = tween(1000)

            )
        startNewRound()

    }

    withContext(Dispatchers.Main){
        isBulletRun =true
        delay(500)
        state.gunSound.let { soundResId ->
            val player = MediaPlayer.create(localContext, soundResId?:0)
            player.start()
            player.setOnCompletionListener {
                it.release()
            }
        }
        delay(1000)
        bulletWordState=true
        delay(1000)
        bulletWordState = false

    }
    back()


}
//    LaunchedEffect(isBulletRun) {
//        state.gunSound.let { soundResId ->
//            val player = MediaPlayer.create(localContext, soundResId?:0)
//            player.start()
//            player.setOnCompletionListener {
//                it.release()
//            }
//        }
//    }
}
@Composable
@Preview(device = "spec:parent=pixel_5,orientation=landscape", showSystemUi = true,
    showBackground = true
)
fun LiarProcessScreenPreview(){
    val player = LanUserEntity(
        id = 1,
        ipAddress = "192.168.1.2",
        name = "Player 1",
        image =" https://robohash.org/ahmed?set=set5",
        isAlive = true,
        numOfShot = 0,
        remainingBullets = 6,
        cards = listOf(
            Card(id = 1, rank =Rank.ACE, imageCard = 1, colorHex ="" ),
            Card(id = 2, rank =Rank.ACE, imageCard = 1, colorHex ="" )
        ),
        isHost = true
    )
    val state = TablePlayersState(looser =  player, spokenText = "lucky guy , crowinho",
        isRealBullet = true,
        gunSound = R.raw.gun_shot ,



        )
}