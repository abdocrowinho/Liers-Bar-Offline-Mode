package com.example.liersbarofflinemode.ui.composable.startScreen.Composable

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.content.contentReceiver
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.data.DataSource.localeDataSource.RepoImpl.GamePlayRepoImpl
import com.example.data.DataSource.localeDataSource.RepoImpl.LanGamePlayRepoImpl
import com.example.domain.Entitys.RoomEntity
import com.example.domain.Repo.LanGamePLay
import com.example.domain.UseCase.GetPlayersStateUseCase
import com.example.liersbarofflinemode.R
import com.example.liersbarofflinemode.ui.Intent.StartScreenIntent
import com.example.liersbarofflinemode.ui.States.Error
import com.example.liersbarofflinemode.ui.States.Loading
import com.example.liersbarofflinemode.ui.States.StartScreenState
import com.example.liersbarofflinemode.ui.States.Success
import com.example.liersbarofflinemode.ui.Utltiy.CustomOutLineBorder
import com.example.liersbarofflinemode.ui.Utltiy.GetHeightConf
import com.example.liersbarofflinemode.ui.Utltiy.GetWidthConf
import com.example.liersbarofflinemode.ui.Utltiy.LanDialogResponse
import com.example.liersbarofflinemode.ui.ViewModels.EndGameDialogViewModel
import com.example.liersbarofflinemode.ui.ViewModels.StartScreenViewModel

@Composable
fun RoomsDialog(
    modifier: Modifier,
    uiState: StartScreenState, action: () -> Unit
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
            .height(GetHeightConf() * .5f)
            .clip(RoundedCornerShape(15.dp))
            .border(
                color = colorResource(id = R.color.dark_red),
                width = 1.dp,
                shape = RoundedCornerShape(15.dp)
            )
    )
    {
        Column(Modifier.fillMaxSize() , horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
            ) {

            when (uiState) {
                is Success -> {
                    CustomContainerButton(
                        text = (uiState ).date.hostName,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                            action()
                    }
                }

                is Loading -> {
                    CustomLoadingIndicator(Modifier.align(Alignment.CenterHorizontally))

                }
                is Error -> {
                    Text(text = uiState.error,
                        color = Color.White ,
                        fontSize = 22.sp ,
                        textAlign = TextAlign.Center
                        , modifier = Modifier.padding(horizontal = 8.dp).align(Alignment.CenterHorizontally),
                        maxLines = 2
                        ) }
            }
        }

    }




}

