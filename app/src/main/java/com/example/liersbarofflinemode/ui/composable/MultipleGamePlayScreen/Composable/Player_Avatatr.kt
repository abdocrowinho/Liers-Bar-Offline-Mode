package com.example.liersbarofflinemode.ui.composable.MultipleGamePlayScreen.Composable

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.domain.Entitys.UserEntity
import com.example.liersbarofflinemode.R
import com.example.liersbarofflinemode.ui.Utltiy.GetHeightConf
import com.example.liersbarofflinemode.ui.Utltiy.GetWidthConf
import com.example.liersbarofflinemode.ui.theme.red_orange
import com.example.liersbarofflinemode.ui.theme.warm_peach
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun PlayerAvatar(rotate: Float, playerState:StateFlow<UserEntity?>,onFireClick : ()->Unit) {


val playerStateCollect by playerState.collectAsState()

    Row(horizontalArrangement = Arrangement.Center,
        modifier = Modifier.rotate(rotate)
    )
    {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AsyncImage(
                model = playerStateCollect?.image,
                contentDescription = "Player Avatar",
                modifier = Modifier
                    .size(75.dp)
                    .border(3.dp, red_orange, shape = RoundedCornerShape(10)),
            )
       playerStateCollect?.name?.let {
           Text(text = it, fontSize = 18.sp, color = warm_peach)
       }

        }

        Spacer(modifier = Modifier.width(GetWidthConf() * .003f))


        LazyColumn(verticalArrangement = Arrangement.Center ) {
            items(playerStateCollect?.remainingBullets?:0) { index ->
                Image(
                    painter = painterResource(id = R.drawable.bullet_left_destention),
                    contentDescription = "bullet $index",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .height(GetHeightConf() * .025f)
                        .width(GetWidthConf() * .03f)
                        .padding(vertical = 1.dp)
                )
                Spacer(modifier = Modifier.height(GetHeightConf() *.008f))
            }

        }
        Spacer(modifier = Modifier.width(GetWidthConf() * .003f))

        Button(
            onClick = {
                onFireClick()
                      },
            modifier = Modifier
                .height(GetHeightConf() * .19f)
                .width(GetWidthConf() * .057f)
            , shape = RoundedCornerShape(12),
            colors = ButtonColors(contentColor = warm_peach,
                disabledContentColor = warm_peach,
                containerColor = red_orange,
                disabledContainerColor = warm_peach
            )
        ) {
            Text(
                text = "f\ni\nr\ne",
                fontSize = 17.sp,
                lineHeight =6.sp,
                textAlign = TextAlign.Center


            )
        }

    }




}


@Preview(
    showSystemUi = true,

    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
    name = "playerPreview", device = "spec:parent=pixel_5,orientation=landscape"
)
@Composable
fun PlayerAvatarPreview() {
    val state = MutableStateFlow<UserEntity?>(null)
    PlayerAvatar(playerState = state, rotate = 0f,){}
}
