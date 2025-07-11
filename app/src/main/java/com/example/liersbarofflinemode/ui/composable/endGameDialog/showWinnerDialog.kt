package com.example.liersbarofflinemode.ui.composable.endGameDialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.domain.Entitys.UserEntity
import com.example.liersbarofflinemode.R
import com.example.liersbarofflinemode.ui.Utltiy.GetHeightConf
import com.example.liersbarofflinemode.ui.Utltiy.GetWidthConf
import com.example.liersbarofflinemode.ui.theme.red_orange

@Composable
fun ShowWinnerDialog(playerWinner:List< UserEntity>?, modifier: Modifier) {
    Card( colors =
    CardColors(containerColor = colorResource(id = R.color.trans_black),
        contentColor = colorResource(id = R.color.trans_black),
        disabledContentColor = colorResource(id = R.color.trans_black),
        disabledContainerColor =  colorResource(id = R.color.trans_black))
        ,modifier = modifier
        .width(GetWidthConf() * .8f)
        .height(GetHeightConf() * .8f)
        .clip(RoundedCornerShape(15.dp))
        .border(
            color = colorResource(id = R.color.dark_red),
            width = 1.dp,
            shape = RoundedCornerShape(15.dp)
        )
    )
    {
        Column(modifier = Modifier.padding(horizontal = 32.dp
            , )
        , verticalArrangement =Arrangement.Top
            , horizontalAlignment = Alignment.Start

        ) {
                PlayerStateBar(playerWinner!!)
                EndGameActionsButtons()
        }
    }


}

@Composable
fun EndGameActionsButtons() {
    Row(horizontalArrangement =
    Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .height(
                GetHeightConf() * .08f
            )
    ) {
        TextButton(onClick = { /*TODO*/ }, modifier = Modifier.fillMaxHeight()) {
            Text(text = "back" , color = Color.White , modifier = Modifier.fillMaxHeight())
        }
        TextButton(onClick = { /*TODO*/ }) {
            Text(text = "Play Again" , color = Color.White)
        }

    }

}

@Composable
fun PlayerStateBar(list: List<UserEntity>){
   val listReversed = list.reversed()
listReversed.forEachIndexed{i,user ->
    Row(modifier = Modifier
        .padding(vertical = 5.dp)
        .fillMaxWidth() , Arrangement.SpaceBetween )

    {
        Row {
            Text(text = (i+1).toString(), color = Color.White, modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(end = 8.dp))
            AsyncImage(model = user.image, contentDescription ="userimage$i" ,
                contentScale = ContentScale.FillBounds , modifier = Modifier
                    .size(55.dp)
                    .border(3.dp, red_orange, shape = RoundedCornerShape(15.dp))
            )
            Text(text = user.name.toString(), color = Color.White, modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 8.dp))

        }


        Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.align(Alignment.CenterVertically)) {
            for (i in 1..user.numOfShot!!){

                Image(painter = painterResource(id = R.drawable.bullet_top_destention)
                    , contentDescription = "deadlyBullet$i",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .width(GetWidthConf() * .015f)
                        .height(GetHeightConf() * .08f)
                    )
                Spacer(modifier = Modifier.width(GetWidthConf() * .005f))
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
            UserEntity(name = "abdo", numOfShot = 2),UserEntity(name = "abdo", numOfShot = 3)
        ,UserEntity(name = "abdo", numOfShot = 6))
    )
}