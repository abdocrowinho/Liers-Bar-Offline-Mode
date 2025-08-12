package com.example.liersbarofflinemode.ui.composable.startScreen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.liersbarofflinemode.R
import com.example.liersbarofflinemode.ui.Intent.StartScreenIntent
import com.example.liersbarofflinemode.ui.States.Error
import com.example.liersbarofflinemode.ui.States.GetRoom
import com.example.liersbarofflinemode.ui.States.HideLanDialog
import com.example.liersbarofflinemode.ui.States.Loading
import com.example.liersbarofflinemode.ui.States.NavigationState
import com.example.liersbarofflinemode.ui.States.ShowLanDialog
import com.example.liersbarofflinemode.ui.States.Success
import com.example.liersbarofflinemode.ui.ViewModels.StartScreenViewModel
import com.example.liersbarofflinemode.ui.composable.startScreen.Composable.ActionsInMainActivity
import com.example.liersbarofflinemode.ui.composable.startScreen.Composable.RoomsDialog
import com.example.liersbarofflinemode.ui.composable.startScreen.Composable.ShowLanDialog
import com.example.liersbarofflinemode.ui.theme.mainActivity.ui.Bases.NavigationItem

@Composable
fun MyApp(
    modifier: Modifier, navController: NavHostController? = null,
    viewModel: StartScreenViewModel = hiltViewModel()
) {

    val navigationState by viewModel.navigationState.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    var text by remember { mutableStateOf("") }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                viewModel.handleIntent(StartScreenIntent.OnBoxClick, "")
            }
            .paint(
                painterResource(id = R.drawable.main_screen),
                contentScale = ContentScale.FillBounds
            )


    ) {
        LaunchedEffect(key1 = navigationState) {
            when (navigationState) {
                is NavigationState.GoingToGame -> {
                    navController?.navigate(NavigationItem.LanGamePlay.route)
                }

                is NavigationState.ShowError -> {
                    Log.d(
                        "error connection in presintation", "MyApp:" +
                                " ${(navigationState as NavigationState.ShowError).error}"
                    )
                }

                null -> {}
            }


        }
        ActionsInMainActivity(modifier = Modifier, navController, viewModel)
        when (uiState) {
            is ShowLanDialog -> {
                ShowLanDialog(modifier = modifier.align(Alignment.Center),
                    viewModel,
                    name = text,
                    onValueChange = { newText -> text = newText })
            }

            is HideLanDialog -> {
                Text(text = "")
            }

            is GetRoom, is Success, is Loading, is Error -> {
                RoomsDialog(modifier = Modifier.align(Alignment.Center), uiState) {
                    viewModel.handleIntent(StartScreenIntent.Join, "")

                }

            }

        }

    }

}
