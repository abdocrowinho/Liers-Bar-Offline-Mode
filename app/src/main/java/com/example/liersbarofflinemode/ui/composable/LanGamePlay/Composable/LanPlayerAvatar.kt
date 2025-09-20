
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.domain.Entitys.LanUserEntity
import com.example.domain.Utlites.getMyIpAddress
import com.example.liersbarofflinemode.R
import com.example.liersbarofflinemode.ui.Utltiy.GetHeightConf
import com.example.liersbarofflinemode.ui.Utltiy.GetWidthConf
import com.example.liersbarofflinemode.ui.theme.red_orange
import com.example.liersbarofflinemode.ui.theme.warm_peach

@Composable
fun LanPlayerAvatar(rotate: Float, playerState: LanUserEntity,modifier: Modifier , size:Dp ) {



    Row(horizontalArrangement = Arrangement.Center,
        modifier = Modifier.rotate(rotate)
    )
    {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AsyncImage(
                model = playerState.image,
                contentDescription = "Player Avatar",
                modifier = Modifier
                    .size(size)
                    .border(3.dp, red_orange, shape = RoundedCornerShape(10)),
            )
            Text(text = playerState.name, fontSize = 18.sp, color = warm_peach)

        }

        Spacer(modifier = Modifier.width(GetWidthConf() * .003f))


        LazyColumn(verticalArrangement = Arrangement.Center ) {
            items(playerState.remainingBullets) { index ->
                Image(
                    painter = painterResource(id = R.drawable.bullet_left_destention),
                    contentDescription = "bullet $index",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .height(size * .12f)
                        .width(size * .20f)
                        .padding(vertical = 1.dp)
                )
                Spacer(modifier = Modifier.height(GetHeightConf() *.008f))
            }

        }
        Spacer(modifier = Modifier.width(GetWidthConf() * .003f))



        }

    }







@Preview(
    showSystemUi = true,

    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
    name = "playerPreview", device = "spec:parent=pixel_5,orientation=landscape"
)
@Composable
fun PlayerAvatarPreview() {
    LanPlayerAvatar(playerState = LanUserEntity(1, getMyIpAddress(),"",
         isAlive = true, remainingBullets =  6,
      numOfShot =   5, image = "" ,  cards =  emptyList(), isHost = true) , rotate = 0f, modifier = Modifier, size = 70.dp)
}
