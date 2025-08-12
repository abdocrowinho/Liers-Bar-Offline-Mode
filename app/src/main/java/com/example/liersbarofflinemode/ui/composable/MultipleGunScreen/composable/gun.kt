package com.example.liersbarofflinemode.ui.composable.MultipleGunScreen.composable

import android.service.autofill.OnClickAction
import android.util.Log
import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.liersbarofflinemode.R
import com.example.liersbarofflinemode.ui.Intent.GamePlayIntent
import com.example.liersbarofflinemode.ui.Utltiy.GetHeightConf
import com.example.liersbarofflinemode.ui.Utltiy.GetWidthConf
import com.example.liersbarofflinemode.ui.ViewModels.GamePlayViewModel

@Composable
fun Gun (modifier: Modifier , userid:Int, navController:NavController , onClickAction: ()->Unit ,
         ){
    Image(painter = painterResource(id = R.drawable.gun),
        contentDescription = "gun",
        modifier = modifier

            .clickable {
                onClickAction()
            }
            .width(GetWidthConf() * .5f)
            .height(GetHeightConf() * .9f),
        contentScale = ContentScale.FillBounds
    )
}