package com.example.liersbarofflinemode.ui.theme.mainActivity.ui.Bases

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.liersbarofflinemode.ui.composable.gamePlayScreen.SinglePlayerScreen
import com.example.liersbarofflinemode.ui.composable.startScreen.MyApp
import com.example.liersbarofflinemode.ui.theme.mainActivity.ui.Bases.Screens.PlayersName.MultiplePlayersScreenBody



    @Composable
    fun AppNavHost(
        navController: NavHostController,
        startDestination: String = NavigationItem.MainActivity.route
    ) {
        NavHost(navController = navController, startDestination = startDestination) {
            composable(NavigationItem.MainActivity.route) {
                MyApp(modifier = Modifier, navController)
            }
            composable(NavigationItem.PlayersNameActivity.route){
            MultiplePlayersScreenBody()
            }
            composable(NavigationItem.SinglePlayersScreen.route){
                SinglePlayerScreen()
            }


        }
    }



 enum class ScreensNames {
    HOME,
    PLAYERS_NAME,
    GAME_PLAY,
    SINGLE_PLAYER_NAME,

}

sealed class NavigationItem(val route: String) {
    data object MainActivity : NavigationItem(ScreensNames.HOME.name)
    data object PlayersNameActivity : NavigationItem(ScreensNames.PLAYERS_NAME.name)
    data object GamePlayScreen : NavigationItem(ScreensNames.GAME_PLAY.name)
    data object SinglePlayersScreen : NavigationItem(ScreensNames.SINGLE_PLAYER_NAME.name)
}
