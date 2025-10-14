package com.example.liersbarofflinemode.ui.composable.LanGamePlay.Composable
import LanPlayerAvatar
import ThrowCardsAnimation
import android.media.MediaPlayer
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.domain.Utlites.getMyIpAddress
import com.example.liersbarofflinemode.R
import com.example.liersbarofflinemode.ui.Intent.EventsLanGamePlayIntent
import com.example.liersbarofflinemode.ui.Utltiy.GetHeightConf
import com.example.liersbarofflinemode.ui.Utltiy.GetWidthConf
import com.example.liersbarofflinemode.ui.Utltiy.getCardRank
import com.example.liersbarofflinemode.ui.ViewModels.LanGamePlayViewModel
import com.example.liersbarofflinemode.ui.theme.warm_peach
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun TablePlayers(lanGamePlayViewModel: LanGamePlayViewModel,
                 navController: NavController) {
    val context = LocalContext.current

    val playersState by lanGamePlayViewModel.players.collectAsState()
    val tableState by lanGamePlayViewModel.uiState.collectAsState()
    val tts = remember { createTts(context) }
    var mediaPlayer: MediaPlayer? by remember { mutableStateOf(null) }
var iswarning by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        lanGamePlayViewModel.oneShot.collect{event->
            when(event){
                LanGamePlayViewModel.UiOneShot.PlayWarningSound ->{
                    iswarning = true
                    mediaPlayer?.release()
                    mediaPlayer = MediaPlayer.create(context, R.raw.warning).apply {
                        isLooping = true
                        start()

                    }

                    launch {
                        delay(3000)
                        mediaPlayer?.apply {
                            stop()
                            release()
                        }
                        iswarning = false
                        lanGamePlayViewModel.clearWarning()
                    }
                }
                is LanGamePlayViewModel.UiOneShot.Speak -> {
                    tts.speak(
                        event.text,
                        TextToSpeech.QUEUE_FLUSH,
                        null,
                        null
                    )
                    if (event.text.isNotEmpty()) {
                        delay(3000)
                        lanGamePlayViewModel.clearSpokenText()
                    }
                }

            }
        }
    }
    // get player Ip adders because we can order players in table
    val myIp = getMyIpAddress()

    // find player active because take action event from his
    val activePlayer = playersState?.find { it?.ipAddress == myIp }

    val allPlayers = playersState?.filterNotNull() ?: emptyList()

    // get ids from all players and sorted it because ordering round with clock rotation
    val ids = allPlayers.map { it.id }.sorted()


    activePlayer?.let { me ->

        val myIndex = ids.indexOf(me.id)
        val rightId = ids.getOrNull((myIndex + 1) % 4)
        val oppositeId = ids.getOrNull((myIndex + 2) % 4)
        val leftId = ids.getOrNull((myIndex + 3) % 4)

        val rightPlayer = allPlayers.find { it.id == rightId }
        val oppositePlayer = allPlayers.find { it.id == oppositeId }
        val leftPlayer = allPlayers.find { it.id == leftId }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .paint(
                    painter = painterResource(id = R.drawable.game_play_background),
                    contentScale = ContentScale.FillBounds
                )
        ) {
            WarningBox(isWarning = iswarning)

            LanPlayerAvatar(
                rotate = 0f,
                playerState = me,
                modifier = Modifier.offset(0.dp, 0.dp),
                size = 70.dp
            )
            VerticalPlayer(
                modifier = Modifier.align(Alignment.Center),
                oppositePlayer = oppositePlayer
            )
            HorizontalPlayers(modifier = Modifier.align(Alignment.Center), rightPlayer, leftPlayer)

            PlayerCards(
                modifier = Modifier.align(Alignment.BottomCenter),
                lanGamePlayViewModel = lanGamePlayViewModel, activePlayer = me
            )

            if (tableState.isFindCardsInTable) {
                Image(
                    painter = painterResource(id = com.example.domain.R.drawable.card_back),
                    contentDescription = "Card back",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .width(GetWidthConf() * .5f)
                        .height(GetHeightConf() * .2f)
                )
            }
            Text(
                text = "Round ${tableState.roundCounter} ",
                modifier = Modifier.align(Alignment.BottomStart),
                fontSize = 25.sp,
                color = warm_peach
            )

            tableState.tableBase?.let {
                getCardRank(it)?.let { it1 -> painterResource(id = it1) }?.let { it2 ->
                    Image(
                        painter = it2,
                        alignment = Alignment.TopEnd, contentDescription = "tableBase",
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = (-20).dp, y = 20.dp)
                            .height(GetHeightConf() * .2f)
                            .width(GetWidthConf() * .5f)
                    )
                }

            }


            ActionButtonInTable(
                lanGamePlayViewModel = lanGamePlayViewModel,
                isMyTurn = tableState.isMyTurn,
                text = "Throw",
                intent = EventsLanGamePlayIntent.ThrowCardsButton(activePlayer.id),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = GetWidthConf() * .02f * -1, y = GetHeightConf() * .02f * -1)
            )
            ActionButtonInTable(
                lanGamePlayViewModel = lanGamePlayViewModel,
                isMyTurn = tableState.isMyTurn,
                text = "Liar",
                intent = EventsLanGamePlayIntent.CallLiarButton(activePlayer.id),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = GetWidthConf() * .02f * -1, y = GetHeightConf() * .15f * -1)

            )
            Image(painter = painterResource(id = R.drawable.warinig_image),
                contentDescription = "warning",
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.BottomEnd)
                    .offset(
                        x = GetWidthConf() * .02f * -1,
                        y = GetHeightConf() * .28f * -1
                    )
                    .clickable {
                        Log.d("warning", "clicked")
                        lanGamePlayViewModel.handleEvents(eventIntent = EventsLanGamePlayIntent.Warning)
                    }
            )

            when (tableState.cardPlayerIdState) {
                activePlayer.id -> {
                    ThrowCardsAnimation(
                        triggerAnimation = tableState.showCardsState,
                        direction = ThrowDirection.BOTTOM
                    ) {
                        lanGamePlayViewModel.clearShowCards()
                    }

                }
                // we need to implement this cases
                rightPlayer?.id -> {
                    ThrowCardsAnimation(
                         triggerAnimation = tableState.showCardsState,
                        direction = ThrowDirection.RIGHT
                    ) {
                        lanGamePlayViewModel.clearShowCards()

                    }

                }

                oppositePlayer?.id -> {
                    ThrowCardsAnimation(
                        triggerAnimation = tableState.showCardsState,

                        direction = ThrowDirection.TOP
                    ) {
                        lanGamePlayViewModel.clearShowCards()
                    }

                }

                leftPlayer?.id -> {
                    ThrowCardsAnimation(
                        triggerAnimation = tableState.showCardsState,
                        direction = ThrowDirection.LEFT
                    ) {
                        lanGamePlayViewModel.clearShowCards()

                    }
                }
            }

            if (tableState.spokenText?.isNotEmpty() == true) {
                Text(
                    text = tableState.spokenText?:"", modifier = Modifier
                        .align(Alignment.Center)
                        .offset(y = (-60).dp),
                    color = Color.White,
                    fontSize = 25.sp
                )
            }
        }



    }

    LaunchedEffect(key1 = tableState.roundCounter){
        if (allPlayers.size==4) {
            lanGamePlayViewModel.handleEvents(EventsLanGamePlayIntent.DealCards)
        }
    }


}


