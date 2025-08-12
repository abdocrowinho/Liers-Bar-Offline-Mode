package com.example.liersbarofflinemode.ui.composable.MultipleGunScreen

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.data.DataSource.localeDataSource.RepoImpl.GamePlayRepoImpl
import com.example.domain.UseCase.GetPlayersStateUseCase
import com.example.domain.UseCase.KillGameUseCase
import com.example.domain.UseCase.SetPlayersUseCase
import com.example.liersbarofflinemode.ui.Intent.GamePlayIntent
import com.example.liersbarofflinemode.ui.Utltiy.GetWidthConf
import com.example.liersbarofflinemode.ui.ViewModels.GamePlayViewModel
import com.example.liersbarofflinemode.ui.composable.MultipleGunScreen.composable.AnimatedBullet
import com.example.liersbarofflinemode.ui.composable.MultipleGunScreen.composable.FireEffectAnimation
import com.example.liersbarofflinemode.ui.composable.MultipleGunScreen.composable.Gun
import com.example.liersbarofflinemode.ui.composable.MultipleGunScreen.composable.NumberBulletsRow

@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
fun MultipleGunScreen(viewModel: GamePlayViewModel = hiltViewModel(), navController: NavController, userid: Int?=0) {
    val context = LocalContext.current
    val gunShotState by viewModel.gunShotState.collectAsState()
val fireCounter = viewModel.fireCounter.collectAsState()
    val xBulletOffset by animateDpAsState(
        targetValue = if (gunShotState[userid]?.moveFireBullet == true) GetWidthConf() * -1 else 0.dp,
        animationSpec = tween(durationMillis = 5000), label = ""
    )

    val yWordStateOffset by animateDpAsState(
        targetValue = if (gunShotState[userid]?.bulletStateWord != null) GetWidthConf() * -1 else 0.dp,
        animationSpec = tween(durationMillis = 1400), label = "wordState"
    )
    LaunchedEffect(gunShotState[userid]?.userAfterShot?.remainingBullets) {
        gunShotState[userid]?.bulletVoice?.let { soundResId ->
            val player = MediaPlayer.create(context, soundResId)
            player.start()
            player.setOnCompletionListener {
                it.release()
            }
        }
    }
    val player = viewModel.playersState[userid]
    val backgroundColor = Color.Black

    Box(
        Modifier
            .fillMaxSize()
            .background(color = backgroundColor)
    ) {
        NumberBulletsRow(modifier = Modifier.align(Alignment.TopCenter), playerState = player)
        AnimatedBullet(modifier = Modifier.align(Alignment.Center), xOffset = xBulletOffset)

        FireEffectAnimation(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(
                    x = (-300).dp,
                    y = (-80).dp
                ), isDeadlyBullet = gunShotState[userid]?.moveFireBullet ?: false
        )
        Gun(
            modifier = Modifier.align(Alignment.Center),
            userid = userid?:0, navController = navController,
        ){
            Log.d("gun counter", fireCounter.value.toString())
            if (fireCounter.value == 0) {
                viewModel.handleIntent(GamePlayIntent.FireBullet(userid?:0)) {
                    navController.popBackStack()
                }

            } else {
                return@Gun
            }
        }
        AnimatedVisibility(visible =gunShotState[userid]?.bulletStateWord != null,
            enter = fadeIn(
            ), exit = fadeOut(animationSpec = tween(1400))
            , modifier = Modifier.align(Alignment.BottomCenter),
        ) {
            Text(text = gunShotState[userid]?.bulletStateWord ?:""
                ,
                fontSize = 23.sp,
                color = Color.White,

            )
        }


    }


}


@Preview(
    device = "spec:parent=pixel_5,orientation=landscape", showSystemUi = true,
    showBackground = true, backgroundColor = 0xFF464746
)
@Composable
fun GunScreenPreview() {
    val savedStateHandle = SavedStateHandle()
    MultipleGunScreen(
        userid = 1,
        viewModel = GamePlayViewModel(SetPlayersUseCase(gamePlayRepo = GamePlayRepoImpl()),
            getPlayersUseCase = GetPlayersStateUseCase(gamePlayRepo = GamePlayRepoImpl()),
            killGameUseCase = KillGameUseCase(gamePlayRepo = GamePlayRepoImpl()) ),
        navController = rememberNavController(),

    )
}