package com.example.liersbarofflinemode.ui.composable.gunScreen.composable

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.navigation.NavController
import com.example.liersbarofflinemode.R
import com.example.liersbarofflinemode.ui.Intent.GamePlayIntent
import com.example.liersbarofflinemode.ui.Utltiy.GetHeightConf
import com.example.liersbarofflinemode.ui.Utltiy.GetWidthConf
import com.example.liersbarofflinemode.ui.ViewModels.GamePlayViewModel
import com.example.liersbarofflinemode.ui.theme.mainActivity.ui.Bases.NavigationItem
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun Gun (modifier: Modifier , userid:Int, navController:NavController , viewModel:GamePlayViewModel){
    val fireCounter = viewModel.fireCounter.collectAsState()
    Image(painter = painterResource(id = R.drawable.gun),
        contentDescription = "gun",
        modifier = modifier

            .clickable {
                Log.d("gun counter", fireCounter.value.toString())
                if (fireCounter.value == 0) {
                    viewModel.handleIntent(GamePlayIntent.FireBullet(userid)) {
                        navController.popBackStack()
                        }

                } else {
                    return@clickable
                }

            }
            .width(GetWidthConf() * .5f)
            .height(GetHeightConf() * .9f),
        contentScale = ContentScale.FillBounds
    )
}