package com.example.liersbarofflinemode.ui.composable.LanGamePlay.Composable

import LanPlayerAvatar
import ThrowCardsAnimation
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.domain.Entitys.LanUserEntity
import com.example.domain.Entitys.Rank
import com.example.domain.GameEvents.CardPlayEvent
import com.example.domain.GameEvents.RoomStateEvent
import com.example.domain.Utlites.getMyIpAddress
import com.example.liersbarofflinemode.R
import com.example.liersbarofflinemode.ui.Intent.EventsLanGamePlayIntent
import com.example.liersbarofflinemode.ui.Utltiy.GetHeightConf
import com.example.liersbarofflinemode.ui.Utltiy.GetWidthConf
import com.example.liersbarofflinemode.ui.ViewModels.LanGamePlayViewModel
import com.example.liersbarofflinemode.ui.theme.warm_peach
import java.util.Locale

@Composable
fun TablePlayers(lanGamePlayViewModel: LanGamePlayViewModel) {

    val context = LocalContext.current

    val playersState by lanGamePlayViewModel.players.collectAsState()
    var showCardsState by remember { mutableStateOf(false) }
    var cardCountState by remember { mutableIntStateOf(0) }
    var cardPlayerIdState by remember { mutableStateOf(-1) }
    var isFindCardsInTable by remember { mutableStateOf(false) }
    var isMyTurn by remember { mutableStateOf(false) }
    var tableBase by remember { mutableStateOf<Rank?>(null) }
    var roundCounter by remember { mutableStateOf(-1) }
    var spokenText by remember { mutableStateOf("") }


    val tts = remember {
        var tempTts: TextToSpeech? = null
        tempTts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tempTts?.language = Locale("en", "UK")
                tempTts?.setPitch(0.1f)
                tempTts?.setSpeechRate(1.0f)
            }
            val voice = tempTts?.voices?.find {
                it.locale.language == "en" &&
                        !it.name.contains("female", true) &&
                        (it.name.contains("en-gb", true) || it.name.contains("en-us", true))
            }

            voice?.let { tempTts?.voice = it }

        }
        tempTts
    }


    LaunchedEffect(spokenText) {
        tts.speak(
            spokenText,
            TextToSpeech.QUEUE_FLUSH,
            null,
            null
        )
        if (spokenText.isNotEmpty()) {
            kotlinx.coroutines.delay(3000)
            spokenText = ""
        }
    }


    // get player Ip adders because we can order players in table
    val myIp = getMyIpAddress()

    // find player active because take action event from his
    val activePlayer = playersState?.find { it?.ipAddress == myIp }

    val allPlayers = playersState?.filterNotNull() ?: emptyList()

    // get ids from all players and sorted it because ordering round with clock rotation
    val ids = allPlayers.map { it.id }.sorted()

    fun getTableBase(rank: Rank?): Int? {
        val rs = when (rank) {
            Rank.ACE -> com.example.domain.R.drawable.ace
            Rank.KING -> com.example.domain.R.drawable.king
            Rank.QUEEN -> com.example.domain.R.drawable.queen
            Rank.JACK -> com.example.domain.R.drawable.jack
            Rank.Joker -> com.example.domain.R.drawable.joker
            null -> null
        }
        return rs
    }

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
                lanGamePlayViewModel = lanGamePlayViewModel, activePlayer = activePlayer
            )

            if (isFindCardsInTable) {
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
                text = "Round $roundCounter ",
                modifier = Modifier.align(Alignment.BottomStart),
                fontSize = 25.sp,
                color = warm_peach
            )

            tableBase?.let {
                getTableBase(tableBase)?.let { it1 -> painterResource(id = it1) }?.let { it2 ->
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
                isMyTurn = isMyTurn,
                text = "Throw",
                intent = EventsLanGamePlayIntent.ThrowCardsButton(activePlayer.id),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = GetWidthConf() * .02f * -1, y = GetHeightConf() * .02f * -1)
            )
            ActionButtonInTable(
                lanGamePlayViewModel = lanGamePlayViewModel,
                isMyTurn = isMyTurn,
                text = "Liar",
                intent = EventsLanGamePlayIntent.ThrowCardsButton(activePlayer.id),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = GetWidthConf() * .02f * -1, y = GetHeightConf() * .15f * -1)

            )

            when (cardPlayerIdState) {
                activePlayer.id -> {
                    ThrowCardsAnimation(
                        numberOfCards = cardCountState, triggerAnimation = showCardsState,
                        direction = ThrowDirection.BOTTOM
                    ) {
                        showCardsState = false
                    }

                }
                // we need to implement this cases
                rightPlayer?.id -> {
                    ThrowCardsAnimation(
                        numberOfCards = cardCountState, triggerAnimation = showCardsState,
                        direction = ThrowDirection.RIGHT
                    ) {
                        showCardsState = false
                    }

                }

                oppositePlayer?.id -> {
                    ThrowCardsAnimation(
                        numberOfCards = cardCountState, triggerAnimation = showCardsState,
                        direction = ThrowDirection.TOP
                    ) {
                        showCardsState = false
                    }

                }

                leftPlayer?.id -> {
                    ThrowCardsAnimation(
                        numberOfCards = cardCountState, triggerAnimation = showCardsState,
                        direction = ThrowDirection.LEFT
                    ) {
                        showCardsState = false
                    }
                }
            }

            if (spokenText.isNotEmpty()) {
                Text(
                    text = spokenText, modifier = Modifier
                        .align(Alignment.Center)
                        .offset(y = (-60).dp),
                    color = Color.White,
                    fontSize = 25.sp
                )

            }
        }


    }

    if (allPlayers.isNotEmpty()) {
        lanGamePlayViewModel.handleEvents(EventsLanGamePlayIntent.DealCards)
    }
    LaunchedEffect(Unit) {
        lanGamePlayViewModel.eventChannel.collect { event ->
            if (event is CardPlayEvent) {
                cardCountState = event.card.size
                showCardsState = true
                cardPlayerIdState = event.playerId
                isFindCardsInTable = true
                Log.d("event in table ", "TablePlayers: $event")
                spokenText = "${event.card.size} ${tableBase?.name}"
                isMyTurn = activePlayer?.id == event.nextPlayer
            }
            if (event is RoomStateEvent && event.tableBase != null) {
                tableBase = event.tableBase?.rank
                roundCounter = event.round ?: 0
                isMyTurn = activePlayer?.id == event.round
            }

        }
    }
}

@Composable
fun HorizontalPlayers(modifier: Modifier, rightPlayer: LanUserEntity?, leftPlayer: LanUserEntity?) {
    Row(horizontalArrangement = Arrangement.SpaceBetween) {
        rightPlayer?.let {
            LanPlayerAvatar(rotate = 0f, playerState = it, modifier = Modifier, size = 60.dp)

        }
        leftPlayer?.let {
            LanPlayerAvatar(rotate = 0f, playerState = it, modifier = Modifier, size = 60.dp)

        }
    }
}
