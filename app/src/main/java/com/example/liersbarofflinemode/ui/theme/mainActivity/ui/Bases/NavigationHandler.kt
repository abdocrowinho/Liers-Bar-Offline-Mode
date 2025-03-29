package com.example.liersbarofflinemode.ui.theme.mainActivity.Bases

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.liersbarofflinemode.ui.theme.mainActivity.Screens.MainActivity.ActionsInMainActivity


class NavigationHandler{
    @Composable
    fun AppNavHost(
        navController: NavHostController ,
        startDestination: String = NavigationItem.Home.route
    ){
        NavHost(navController = navController, startDestination =startDestination ) {
            composable(NavigationItem.Home.route){
                ActionsInMainActivity(modifier = Modifier, navController = navController)
            }
            composable(NavigationItem.PlayersNameScreen.route){

            }

        }
    }
}


   private enum class Screens{
        HOME,
        PLAYERS_NAME,
        GAME_PLAY ,
        SINGLE_PLAYER_NAME,

    }
    sealed class NavigationItem(val route : String){
        data object  Home : NavigationItem(Screens.HOME.name)
        data object PlayersNameScreen : NavigationItem(Screens.PLAYERS_NAME.name)
        data object GamePlayScreen : NavigationItem(Screens.GAME_PLAY.name)
        data object SinglePlayersScreen : NavigationItem(Screens.SINGLE_PLAYER_NAME.name)
    }
