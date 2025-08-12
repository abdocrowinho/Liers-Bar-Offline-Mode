package com.example.liersbarofflinemode.ui.composable.endGameDialog.Composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.liersbarofflinemode.ui.Intent.EndGameDialogIntent
import com.example.liersbarofflinemode.ui.Utltiy.GetHeightConf
import com.example.liersbarofflinemode.ui.ViewModels.EndGameDialogViewModel
import com.example.liersbarofflinemode.ui.theme.mainActivity.ui.Bases.NavigationItem
import com.example.liersbarofflinemode.ui.theme.mainActivity.ui.Bases.ScreensNames

@Composable
fun EndGameActionsButtons(
    viewModel: EndGameDialogViewModel,
    navController: NavController,
    reset: () -> Unit
) {
    Row(
        horizontalArrangement =
        Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .height(
                GetHeightConf() * .08f
            )
    ) {
        TextButton(onClick = {
            viewModel.handleIntent(EndGameDialogIntent.Back {
                navController.navigate(NavigationItem.MainActivity.route) {
                    popUpTo(NavigationItem.GamePlayScreen.route) {
                        inclusive = true
                        saveState = false
                    }
                }
            })
                             }, modifier = Modifier.fillMaxHeight()) {
            Text(text = "back", color = Color.White, modifier = Modifier.fillMaxHeight())
        }
        TextButton(onClick = {

            viewModel.handleIntent(EndGameDialogIntent.PlayAgainIntent {
                reset()
            })
        }

        ) {
            Text(text = "Play Again", color = Color.White)
        }

    }

}
