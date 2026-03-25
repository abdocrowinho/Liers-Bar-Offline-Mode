package com.example.liersbarofflinemode.ui.composable.endGameDialog

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.data.DataSource.localeDataSource.RepoImpl.GamePlayRepoImpl
import com.example.domain.Entitys.UserEntity
import com.example.domain.UseCase.GetPlayersStateUseCase
import com.example.liersbarofflinemode.R
import com.example.liersbarofflinemode.ui.Utltiy.GetHeightConf
import com.example.liersbarofflinemode.ui.Utltiy.GetWidthConf
import com.example.liersbarofflinemode.ui.ViewModels.EndGameDialogViewModel
import com.example.liersbarofflinemode.ui.composable.endGameDialog.Composable.EndGameActionsButtons
import com.example.liersbarofflinemode.ui.composable.endGameDialog.Composable.PlayerStateBar

@Composable
fun ShowWinnerDialog(
    playerWinner: List<UserEntity>?,
    modifier: Modifier,
    viewModel: EndGameDialogViewModel = hiltViewModel(),
    text: String? = "",
    hasUserName: Boolean? = true,
    navController: NavController?,
    reset: () -> Unit,
) {
    Card(
        colors = CardColors(
            containerColor = colorResource(id = R.color.trans_black),
            contentColor = colorResource(id = R.color.trans_black),
            disabledContentColor = colorResource(id = R.color.trans_black),
            disabledContainerColor = colorResource(id = R.color.trans_black)
        ),
        modifier = modifier
            .wrapContentSize()
            .padding()
            .width(GetWidthConf() * .50f)
            .clip(RoundedCornerShape(15.dp))
            .border(
                color = colorResource(id = R.color.dark_red),
                width = 1.dp,
                shape = RoundedCornerShape(15.dp)
            )
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            // Fix 1: only show PlayerStateBar if list is not null and not empty
            if (!playerWinner.isNullOrEmpty()) {
                PlayerStateBar(playerWinner, text, hasUserName)
            } else {
                // LAN game over — just show winner text
                Text(
                    text = text ?: "",
                    color = Color.White,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }

            // Fix 2: only show buttons if navController is not null
            if (navController != null) {
                EndGameActionsButtons(viewModel, navController, reset)
            } else {
                // Fallback — just show play again button
                TextButton(onClick = { reset() }) {
                    Text(text = "Play Again", color = Color.White)
                }
            }
        }
    }
}

@Composable
@Preview(
    device = "spec:parent=pixel_5,orientation=landscape", showSystemUi = true,
    showBackground = true
)
fun ShowWinnerDialogPreview() {
    ShowWinnerDialog(
        modifier = Modifier,
        playerWinner = listOf(
            UserEntity(name = "abdo", numOfShot = 4),
            UserEntity(name = "abdo", numOfShot = 2),
            UserEntity(name = "abdo", numOfShot = 3),
            UserEntity(name = "abdo", numOfShot = 6)
        ),
        viewModel = EndGameDialogViewModel(
            getPlayersUseCase = GetPlayersStateUseCase(
                GamePlayRepoImpl()
            ),

        ),

        navController = null

    ) {}
}