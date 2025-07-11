package com.example.liersbarofflinemode.ui.composable.startScreen.Composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.data.DataSource.localeDataSource.RepoImpl.GamePlayRepoImpl
import com.example.data.DataSource.localeDataSource.RepoImpl.LanGamePlayRepoImpl
import com.example.domain.Repo.LanGamePLay
import com.example.domain.UseCase.GetPlayersStateUseCase
import com.example.liersbarofflinemode.R
import com.example.liersbarofflinemode.ui.Intent.StartScreenIntent
import com.example.liersbarofflinemode.ui.Utltiy.CustomOutLineBorder
import com.example.liersbarofflinemode.ui.Utltiy.GetWidthConf
import com.example.liersbarofflinemode.ui.ViewModels.EndGameDialogViewModel
import com.example.liersbarofflinemode.ui.ViewModels.StartScreenViewModel
import com.example.liersbarofflinemode.ui.theme.mainActivity.ui.Bases.Screens.MainActivity.Composable.CustomWoodButton
import com.example.liersbarofflinemode.ui.theme.red_orange

@Composable
fun ShowLanDialog(
    modifier: Modifier,
    viewModel: StartScreenViewModel,
    name: String,
    onValueChange: (String) -> Unit
) {
    Card(
        colors =
        CardColors(
            containerColor = colorResource(id = R.color.trans_black),
            contentColor = colorResource(id = R.color.trans_black),
            disabledContentColor = colorResource(id = R.color.trans_black),
            disabledContainerColor = colorResource(id = R.color.trans_black)
        ), modifier = modifier
            .wrapContentSize()
            .width(GetWidthConf() * .50f)
            .clip(RoundedCornerShape(15.dp))
            .border(
                color = colorResource(id = R.color.dark_red),
                width = 1.dp,
                shape = RoundedCornerShape(15.dp)
            )
    )
    {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            OutlinedTextField(value = name, onValueChange = { newText -> onValueChange(newText) },
                textStyle = TextStyle(
                    color = red_orange
                ))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize()
                    .padding(vertical = 15.dp)
                    .background(color = Color.Transparent)
            ) {
                CustomWoodButton(
                    onClick = {
                        viewModel.handleIntent(
                            intent = StartScreenIntent.CreateRoom, name
                        )
                    }, text =
                    "Create Room", width = .2f, height = .15f
                )


                Spacer(modifier = Modifier.width(20.dp))

                CustomWoodButton(
                    onClick = {
                        viewModel.handleIntent(StartScreenIntent.GoingRoom, name)
                    }, text =
                    "Going to Room", width = .2f, height = .15f
                )

            }
        }
    }


}

