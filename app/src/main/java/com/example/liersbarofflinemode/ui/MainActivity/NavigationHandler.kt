package com.example.liersbarofflinemode.ui.theme.mainActivity.ui.Bases

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavArgument
import androidx.navigation.NavHostController
import androidx.navigation.PopUpToBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.domain.Entitys.UserEntity
import com.example.liersbarofflinemode.ui.Utltiy.ArgumentsKeys
import com.example.liersbarofflinemode.ui.ViewModels.EnterUsersScreenViewModel
import com.example.liersbarofflinemode.ui.composable.PlayersNameScreen.EnterUsersScreen
import com.example.liersbarofflinemode.ui.composable.gamePlayScreen.GamePlayScreen
import com.example.liersbarofflinemode.ui.composable.singlePlayerScreen.Composable.SinglePlayerScreen
import com.example.liersbarofflinemode.ui.composable.startScreen.MyApp


@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String = NavigationItem.MainActivity.route
) {

    NavHost(navController = navController, startDestination = startDestination) {
        composable(NavigationItem.MainActivity.route) {
            MyApp(modifier = Modifier, navController)
        }
        composable(NavigationItem.PlayersNameActivity.route) {
            val viewModel: EnterUsersScreenViewModel = hiltViewModel()
            EnterUsersScreen(Modifier, viewModel, navController)
        }
        composable(NavigationItem.GamePlayScreen.route){
            GamePlayScreen(navHostController = navController)
        }


        composable(NavigationItem.SinglePlayersScreen.route) {
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
