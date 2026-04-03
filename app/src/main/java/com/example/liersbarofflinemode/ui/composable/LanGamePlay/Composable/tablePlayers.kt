package com.example.liersbarofflinemode.ui.composable.LanGamePlay.Composable

import LanPlayerAvatar
import ThrowCardsAnimation
import android.media.MediaPlayer
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import com.example.data.DataSource.localeDataSource.deviceIdManger.DeviceIdManager
import com.example.domain.Utlites.LanPlayerState
import com.example.domain.Utlites.MangerLanPlayerState
import com.example.domain.Utlites.getMyIpAddress
import com.example.liersbarofflinemode.R
import com.example.liersbarofflinemode.ui.Intent.EventsLanGamePlayIntent
import com.example.liersbarofflinemode.ui.Utltiy.GetHeightConf
import com.example.liersbarofflinemode.ui.Utltiy.GetWidthConf
import com.example.liersbarofflinemode.ui.Utltiy.getCardRank
import com.example.liersbarofflinemode.ui.ViewModels.LanGamePlayViewModel
import com.example.liersbarofflinemode.ui.composable.endGameDialog.ShowWinnerDialog
import com.example.liersbarofflinemode.ui.theme.warm_peach
import kotlinx.coroutines.delay
import java.util.Locale

@Composable
fun TablePlayers(
    lanGamePlayViewModel: LanGamePlayViewModel,
    navController: NavController
) {
    val context = LocalContext.current

    val playersState by lanGamePlayViewModel.players.collectAsState()
    val tableState by lanGamePlayViewModel.uiState.collectAsState()
    val readyCards by lanGamePlayViewModel.readyCard.collectAsState()

    val timerValue by lanGamePlayViewModel.timerValue.collectAsState()

    val playerState = MangerLanPlayerState.state.collectAsState().value

    val tts = remember { mutableStateOf<TextToSpeech?>(null) }
    DisposableEffect(context) {
        val ttsInstance = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts.value?.language = Locale.ENGLISH  // ✅ always English
            }
        }
        tts.value = ttsInstance
        onDispose {
            ttsInstance.stop()
            ttsInstance.shutdown()
        }
    }

    var mediaPlayer: MediaPlayer? by remember { mutableStateOf(null) }
    var isWarning by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        lanGamePlayViewModel.oneShot.collect { event ->
            when (event) {
                LanGamePlayViewModel.UiOneShot.PlayWarningSound -> {
                    mediaPlayer?.apply {
                        if (isPlaying) stop()
                        release()
                    }
                    mediaPlayer = null
                    isWarning = true
                    mediaPlayer = MediaPlayer.create(context, R.raw.warning).apply {
                        isLooping = false
                        start()
                        setOnCompletionListener {
                            it.release()
                            mediaPlayer = null
                            isWarning = false
                            lanGamePlayViewModel.clearWarning()
                        }
                    }
                }
                is LanGamePlayViewModel.UiOneShot.Speak -> {
                    tts.value?.language = Locale.ENGLISH
                    tts.value?.speak(event.text, TextToSpeech.QUEUE_FLUSH, null, null)
                    if (event.text.isNotEmpty()) {
                        delay(3000)
                        lanGamePlayViewModel.clearSpokenText()
                    }
                }
            }
        }
    }

    val myIp = getMyIpAddress()
    val allPlayers = playersState?.filterNotNull() ?: emptyList()

    val activePlayer = if (playerState == LanPlayerState.Host) {
        allPlayers.find { it.isHost }
    } else {
        val myDeviceId = DeviceIdManager.getDeviceId(context)
        allPlayers.find { it.ipAddress == myIp }
            ?: allPlayers.find { it.deviceId == myDeviceId }
    }

    val ids = allPlayers.map { it.id }.sorted()

    activePlayer?.let { me ->
        val myIndex = ids.indexOf(me.id)
        val count = ids.size

        val rightId = if (count > 1) ids[(myIndex + 1) % count] else null
        val oppositeId = if (count > 2) ids[(myIndex + 2) % count] else null
        val leftId = if (count > 3) ids[(myIndex + 3) % count] else null

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

            WarningBox(isWarning = isWarning)

            LanPlayerAvatar(
                rotate = 0f,
                playerState = me,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(x = 8.dp, y = 8.dp),
                size = 70.dp,
                timerValue = if (tableState.isMyTurn) timerValue else null,
                currentTurnId = tableState.currentTurnId,
                viewModel = lanGamePlayViewModel
            )

            VerticalPlayer(
                modifier = Modifier.align(Alignment.Center),
                oppositePlayer = oppositePlayer,
                isMyTurn = tableState.isMyTurn,
                currentTurnId = tableState.currentTurnId,
                viewModel = lanGamePlayViewModel
            )

            HorizontalPlayers(
                modifier = Modifier.align(Alignment.Center),
                rightPlayer = rightPlayer,
                leftPlayer = leftPlayer,
                isMyTurn = tableState.isMyTurn,
                currentTurnId = tableState.currentTurnId,
                viewModel = lanGamePlayViewModel
            )

            PlayerCards(
                modifier = Modifier.align(Alignment.BottomCenter),
                lanGamePlayViewModel = lanGamePlayViewModel,
                activePlayer = me
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
                text = "Round ${tableState.roundCounter}",
                modifier = Modifier.align(Alignment.BottomStart),
                fontSize = 25.sp,
                color = warm_peach
            )

            tableState.tableBase?.let { rank ->
                getCardRank(rank)?.let { resId ->
                    Image(
                        painter = painterResource(id = resId),
                        alignment = Alignment.TopEnd,
                        contentDescription = "tableBase",
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
                intent = EventsLanGamePlayIntent.ThrowCardsButton(me.id),
                readyCards = readyCards,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(
                        x = GetWidthConf() * .02f * -1,
                        y = GetHeightConf() * .02f * -1
                    )
            )

            ActionButtonInTable(
                lanGamePlayViewModel = lanGamePlayViewModel,
                isMyTurn = tableState.isMyTurn,
                text = "Liar",
                intent = EventsLanGamePlayIntent.CallLiarButton(me.id),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(
                        x = GetWidthConf() * .02f * -1,
                        y = GetHeightConf() * .15f * -1
                    )
            )

            Image(
                painter = painterResource(id = R.drawable.warinig_image),
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
                        lanGamePlayViewModel.handleEvents(EventsLanGamePlayIntent.Warning)
                    }
            )

            when (tableState.cardPlayerIdState) {
                me.id -> ThrowCardsAnimation(
                    triggerAnimation = tableState.showCardsState,
                    direction = ThrowDirection.BOTTOM
                ) { lanGamePlayViewModel.clearShowCards() }
                rightPlayer?.id -> ThrowCardsAnimation(
                    triggerAnimation = tableState.showCardsState,
                    direction = ThrowDirection.RIGHT
                ) { lanGamePlayViewModel.clearShowCards() }
                oppositePlayer?.id -> ThrowCardsAnimation(
                    triggerAnimation = tableState.showCardsState,
                    direction = ThrowDirection.TOP
                ) { lanGamePlayViewModel.clearShowCards() }
                leftPlayer?.id -> ThrowCardsAnimation(
                    triggerAnimation = tableState.showCardsState,
                    direction = ThrowDirection.LEFT
                ) { lanGamePlayViewModel.clearShowCards() }
            }

            if (tableState.spokenText?.isNotEmpty() == true) {
                Text(
                    text = tableState.spokenText ?: "",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .offset(y = (-60).dp),
                    color = Color.White,
                    fontSize = 25.sp
                )
            }

            if (tableState.isGameOver) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.6f)),
                    contentAlignment = Alignment.Center
                ) {
                    ShowWinnerDialog(
                        playerWinner = null,
                        modifier = Modifier,
                        text = "🏆 Winner: ${tableState.winnerName}!",
                        hasUserName = false,
                        navController = navController,
                        reset = { lanGamePlayViewModel.handlePlayAgain() }
                    )
                }
            }

            if (tableState.showBotVoteDialog) {
                ShowBotVoteDialog(
                    players = allPlayers,
                    yesCount = tableState.botVoteYesCount,
                    totalPlayers = tableState.botVoteTotalPlayers,
                    onYes = { lanGamePlayViewModel.submitBotVote(true) },
                    onNo = { lanGamePlayViewModel.submitBotVote(false) }
                )
            }
        }
    }
}