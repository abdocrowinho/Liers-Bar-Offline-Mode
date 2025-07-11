package com.example.liersbarofflinemode.ui.theme.mainActivity.ui.Bases.Screens.MainActivity

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi

import androidx.navigation.compose.rememberNavController
import com.example.liersbarofflinemode.ui.theme.LiersBarOfflineModeTheme
import com.example.liersbarofflinemode.ui.theme.mainActivity.ui.Bases.AppNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

                window.setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN)

            val navController = rememberNavController()

            LiersBarOfflineModeTheme {
                AppNavHost(navController = navController)
            }
        }
    }
}







