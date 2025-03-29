package com.example.liersbarofflinemode.ui.theme.mainActivity.Screens.MainActivity

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import com.example.liersbarofflinemode.R
import com.example.liersbarofflinemode.ui.theme.LiersBarOfflineModeTheme
import com.example.liersbarofflinemode.ui.theme.mainActivity.Screens.MainActivity.Composable.Button

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.insetsController?.let { controller ->
            controller.hide(WindowInsets.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        setContent {
            val modifier = Modifier
            val configuration = LocalConfiguration.current
            val screenHeight = configuration.screenHeightDp.dp
            val screenWidth = configuration.screenWidthDp.dp
            LiersBarOfflineModeTheme {
                Box(
                    modifier = modifier
                        .fillMaxSize()
                        .paint(
                            painterResource(id = R.drawable.main_screen),
                            contentScale = ContentScale.FillBounds
                        )


                ) {
                    ActionsInMainActivity(
                        modifier = modifier.offset(
                            y = screenHeight * .7f,
                            x = screenWidth * .3f
                        )
                    )

                }
            }
        }
    }
}





@Composable
fun ActionsInMainActivity(modifier: Modifier,navController: NavHostController?=null) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier.fillMaxWidth()) {
        Button(onClick = {
        }, text = "multiple")
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = {}, text = "Single")
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = {}, text = "Lan")
    }


}


