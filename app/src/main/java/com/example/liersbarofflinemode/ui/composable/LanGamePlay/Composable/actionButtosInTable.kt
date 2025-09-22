package com.example.liersbarofflinemode.ui.composable.LanGamePlay.Composable

import android.util.Log
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.domain.Entitys.LanUserEntity
import com.example.liersbarofflinemode.ui.Intent.EventsLanGamePlayIntent
import com.example.liersbarofflinemode.ui.ViewModels.LanGamePlayViewModel
import com.example.liersbarofflinemode.ui.theme.red_orange

@Composable
fun ActionButtonInTable(lanGamePlayViewModel : LanGamePlayViewModel ,
                        text : String,
                        intent: EventsLanGamePlayIntent,
                        modifier: Modifier = Modifier,
                        isMyTurn : Boolean
                        ){

    Button(onClick =
    { if (intent is EventsLanGamePlayIntent.ThrowCardsButton){
        lanGamePlayViewModel.
        handleEvents(
            EventsLanGamePlayIntent.
            ThrowCardsButton(playerId =intent.playerId))

    }else if (intent is EventsLanGamePlayIntent.CallLiarButton){
        lanGamePlayViewModel.
        handleEvents(EventsLanGamePlayIntent.
        CallLiarButton(playerId = intent.playerId))
    }

    }
, modifier = modifier
        , colors = ButtonColors(containerColor = red_orange,
            contentColor = Color.White,
            disabledContentColor = Color.White,
            disabledContainerColor = Color.Gray,
            )
        ,

        enabled = true
    ) {
        Text(text = text)
    }


}