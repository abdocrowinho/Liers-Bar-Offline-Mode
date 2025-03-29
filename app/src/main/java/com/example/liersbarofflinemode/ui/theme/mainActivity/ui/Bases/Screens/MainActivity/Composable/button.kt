package com.example.liersbarofflinemode.ui.theme.mainActivity.Screens.MainActivity.Composable

import android.widget.ImageButton
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.liersbarofflinemode.R

@Composable
fun Button(modifier: Modifier = Modifier, onClick: () -> Unit, text: String) {

    Box(modifier = modifier
        .fillMaxHeight(0.08f)
        .fillMaxWidth(.15f)
        .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Image(painter = painterResource(id =  R.drawable.main_button)
            , contentDescription = "asdf",
           contentScale = ContentScale.FillBounds)

        Text(text = text)

    }


}