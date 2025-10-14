package com.example.liersbarofflinemode.ui.theme.mainActivity.ui.Bases

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.liersbarofflinemode.ui.ViewModels.EnterSingleUserViewModel
import com.example.liersbarofflinemode.ui.ViewModels.EnterUsersScreenViewModel
import com.example.liersbarofflinemode.ui.ViewModels.GamePlayViewModel
import com.example.liersbarofflinemode.ui.ViewModels.LanGamePlayViewModel
import com.example.liersbarofflinemode.ui.ViewModels.StartScreenViewModel
import com.example.liersbarofflinemode.ui.composable.EnterPlayersNameScreen.EnterUsersScreen
import com.example.liersbarofflinemode.ui.composable.MultipleGamePlayScreen.GamePlayScreen
import com.example.liersbarofflinemode.ui.composable.MultipleGunScreen.MultipleGunScreen
import com.example.liersbarofflinemode.ui.composable.EnterSinglePlayerScreen.EnterSinglePlayerScreen
import com.example.liersbarofflinemode.ui.composable.LanGamePlay.LanGamePlayScreen
import com.example.liersbarofflinemode.ui.composable.LiarProcessScreen.LiarProcessScreen
import com.example.liersbarofflinemode.ui.composable.SingleGunScreen.SingleGunScreen
import com.example.liersbarofflinemode.ui.composable.SinglePlayerGamePlay.SingleGamePlayScreen
import com.example.liersbarofflinemode.ui.composable.startScreen.MyApp


@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String = NavigationItem.MainActivity.route
) {

    NavHost(navController = navController, startDestination = startDestination) {
        composable(NavigationItem.MainActivity.route) {
            val viewModel : StartScreenViewModel = hiltViewModel()

            MyApp(modifier = Modifier, navController , viewModel)
        }
        composable(NavigationItem.PlayersNameActivity.route) {
            val viewModel: EnterUsersScreenViewModel = hiltViewModel()
            EnterUsersScreen(Modifier, viewModel, navController)
        }
        navigation(
            startDestination = NavigationItem.GamePlayScreen.route,
            route = NavigationItem.GameGraph.route
        ) {

            composable(NavigationItem.GamePlayScreen.route) {
                val gamePlayViewModel: GamePlayViewModel = hiltViewModel()

                GamePlayScreen(navHostController = navController, gamePlayViewModel)
            }
            composable("${NavigationItem.GunScreen.route}/{userid}",
                arguments = listOf(navArgument("userid") {
                    type = NavType.IntType
                }
                )
            ) { backStackEntry ->
                val userid = backStackEntry.arguments?.getInt("userid") ?: 0
                val parentEntry = remember {
                    navController.getBackStackEntry(NavigationItem.GamePlayScreen .route)
                }
                val gamePlayViewModel: GamePlayViewModel = hiltViewModel(parentEntry)

                MultipleGunScreen(gamePlayViewModel, navController, userid)

            }
        }






        navigation(
            startDestination = NavigationItem.SinglePlayersScreen.route,
            route = NavigationItem.SingleGameGraph.route
        ) {
            composable(NavigationItem.SinglePlayersScreen.route) {
                val viewModel: EnterSingleUserViewModel = hiltViewModel()
                EnterSinglePlayerScreen(navController = navController, viewModel = viewModel)

            }
            composable(NavigationItem.SingleGamePlayScreen.route) {
                val parentEntry = navController.getBackStackEntry(NavigationItem.SinglePlayersScreen.route)
                val enterSingleUserViewModel: EnterSingleUserViewModel = hiltViewModel(parentEntry)

                SingleGamePlayScreen(
                    navHostController = navController,
                    viewModel = enterSingleUserViewModel
                )
            }
            composable(NavigationItem.SingleGunScreen.route)
             {
                val parentEntry = navController.getBackStackEntry(NavigationItem.SinglePlayersScreen.route)
                val gamePlayViewModel: EnterSingleUserViewModel = hiltViewModel(parentEntry)
                SingleGunScreen(navController =  navController, viewModel =  gamePlayViewModel)
            }

        }
        navigation(startDestination= NavigationItem.LanGamePlay.route,
          route=  NavigationItem.LanGameGraph.route
            ){
            composable(NavigationItem.LanGamePlay.route){
                val parentEntry = remember {

                 navController.getBackStackEntry(NavigationItem.LanGameGraph.route)}
                val lanGamePlayViewModel: LanGamePlayViewModel = hiltViewModel(parentEntry)
                LanGamePlayScreen(navController , lanGamePlayViewModel)
            }

            composable(NavigationItem.LiarProcessScreen.route){
                val parentEntry = remember {
                 navController.getBackStackEntry(NavigationItem.LanGameGraph.route)}
                val lanGamePlayViewModel : LanGamePlayViewModel = hiltViewModel(parentEntry)
                LiarProcessScreen(navController = navController,lanGamePlayViewModel)
            }
        }

    }
}

enum class ScreensNames {
    HOME,
    PLAYERS_NAME,
    GAME_PLAY,
    GUN_SCREEN,
    SINGLE_PLAYER_NAME,
    SINGLE_GUN_SCREEN,
    SINGLE_GAME_PLAY,
    SINGLE_GAME_GRAPH,
    GAME_GRAPH,
    LAN_GAME_PLAY,
    LIAR_PROCESS,
    LAN_GAME_GRAPH

}

sealed class NavigationItem(val route: String) {
    data object MainActivity : NavigationItem(ScreensNames.HOME.name)
    data object PlayersNameActivity : NavigationItem(ScreensNames.PLAYERS_NAME.name)
    data object GamePlayScreen : NavigationItem(ScreensNames.GAME_PLAY.name)
    data object GunScreen : NavigationItem(ScreensNames.GUN_SCREEN.name)
    data object GameGraph : NavigationItem(ScreensNames.GAME_GRAPH.name)
    data object SingleGunScreen : NavigationItem(ScreensNames.SINGLE_GUN_SCREEN.name)
    data object LanGamePlay : NavigationItem(ScreensNames.LAN_GAME_PLAY.name)
    data object LiarProcessScreen : NavigationItem(ScreensNames.LIAR_PROCESS.name)

    data object LanGameGraph : NavigationItem(ScreensNames.LAN_GAME_GRAPH.name)


    data object SingleGameGraph : NavigationItem(ScreensNames.SINGLE_GAME_GRAPH.name)
    data object SingleGamePlayScreen : NavigationItem(ScreensNames.SINGLE_GAME_PLAY.name)
    data object SinglePlayersScreen : NavigationItem(ScreensNames.SINGLE_PLAYER_NAME.name)


}
