import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.domain.Entitys.LanUserEntity
import com.example.liersbarofflinemode.R
import com.example.liersbarofflinemode.ui.Utltiy.GetHeightConf
import com.example.liersbarofflinemode.ui.Utltiy.GetWidthConf
import com.example.liersbarofflinemode.ui.ViewModels.LanGamePlayViewModel
import com.example.liersbarofflinemode.ui.theme.red_orange
import com.example.liersbarofflinemode.ui.theme.warm_peach

@Composable
fun LanPlayerAvatar(
    rotate: Float,
    playerState: LanUserEntity,
    modifier: Modifier,
    size: Dp,
    // ✅ timerValue: actual countdown number from ViewModel (10→0), null = not my turn
    timerValue: Int? = null,
    currentTurnId: Int? = null,
    viewModel: LanGamePlayViewModel? = null,
) {
    val bulletCount = playerState.remainingBullets.coerceAtLeast(0)
    val isActiveTurn = playerState.id == currentTurnId && playerState.isAlive

    // ✅ Timer color: red when urgent, amber mid-range, green when safe
    val timerColor = when {
        timerValue == null || timerValue <= 0 -> Color.Transparent
        timerValue <= 3 -> Color(0xFFE24B4A)
        timerValue <= 6 -> Color(0xFFEF9F27)
        else -> Color(0xFF639922)
    }

    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Box(contentAlignment = Alignment.Center) {

                // Avatar image — no circle, just border highlight on active turn
                AsyncImage(
                    model = playerState.image,
                    contentDescription = "Player Avatar",
                    modifier = Modifier
                        .size(size)
                        .rotate(rotate)
                        .clip(RoundedCornerShape(10))
                        .border(
                            width = if (isActiveTurn) 2.dp else 1.dp,
                            color = when {
                                !playerState.isAlive -> Color.Gray
                                isActiveTurn -> timerColor  // border matches timer color
                                else -> red_orange
                            },
                            shape = RoundedCornerShape(10)
                        )
                        .graphicsLayer {
                            alpha = if (playerState.isAlive) 1f else 0.4f
                        }
                )

                // ✅ Timer number — shown bottom-center of avatar, only when it's my turn
                if (isActiveTurn && timerValue != null && timerValue > 0) {
                    Text(
                        text = "$timerValue",
                        color = timerColor,
                        fontSize = (size.value * 0.28f).sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .offset(y = (-4).dp)
                            .background(
                                Color.Black.copy(alpha = 0.55f),
                                RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 4.dp, vertical = 1.dp)
                    )
                }
            }

            // Player name / dead label
            Text(
                text = if (playerState.isAlive) playerState.name else "dead",
                fontSize = 14.sp,
                color = if (playerState.isAlive) warm_peach else Color.Gray,
                maxLines = 1
            )
        }

        Spacer(modifier = Modifier.width(GetWidthConf() * .003f))

        // Bullets column
        LazyColumn(verticalArrangement = Arrangement.Center) {
            items(bulletCount) { index ->
                Image(
                    painter = painterResource(id = R.drawable.bullet_left_destention),
                    contentDescription = "bullet $index",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .height(size * .12f)
                        .width(size * .20f)
                        .padding(vertical = 1.dp)
                )
                Spacer(modifier = Modifier.height(GetHeightConf() * .008f))
            }
        }

        Spacer(modifier = Modifier.width(GetWidthConf() * .003f))
    }
}