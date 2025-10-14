package com.example.liersbarofflinemode.ui.composable.LanGamePlay.Composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import com.example.domain.Entitys.Card
import com.example.domain.Entitys.Rank
import com.example.liersbarofflinemode.ui.Utltiy.GetHeightConf
import com.example.liersbarofflinemode.ui.Utltiy.GetWidthConf

@Composable
fun SingleCard(card: Card,modifier: Modifier){
    Box(modifier = modifier)
    {
        Image(painter = painterResource(id =card.imageCard ),
            contentScale = ContentScale.FillBounds,
            contentDescription ="card"
            , modifier =
            Modifier
                .width(width = GetWidthConf() * .07f)
                .height(GetHeightConf() * .25f)
        )
    }
}

