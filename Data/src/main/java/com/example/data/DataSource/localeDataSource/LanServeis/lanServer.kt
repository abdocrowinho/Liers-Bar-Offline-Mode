package com.example.data.DataSource.localeDataSource.LanServeis

import android.util.Log
import com.example.data.DataSource.Utltity.FakeBotSocket
import com.example.data.DataSource.Utltity.FunctionHelper
import com.example.domain.Entitys.Card
import com.example.domain.Entitys.LanUserEntity
import com.example.domain.Entitys.Rank
import com.example.domain.GameEvents.BotVoteStateEvent
import com.example.domain.GameEvents.CardPlayEvent
import com.example.domain.GameEvents.Event
import com.example.domain.GameEvents.GameOverEvent
import com.example.domain.GameEvents.JoinToGameEvent
import com.example.domain.GameEvents.LiarCallEvent
import com.example.domain.GameEvents.LiarCallResult
import com.example.domain.GameEvents.PlayAgainEvent
import com.example.domain.GameEvents.RoomStateEvent
import com.example.domain.GameEvents.WarningEvent
import com.example.domain.Utlites.Constant.listOfCard
import com.example.domain.Utlites.getMyIpAddress
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import java.net.InetSocketAddress

class GameWebSocketServer(port: Int) : WebSocketServer(InetSocketAddress(port)) {

    var roundStarted = MutableStateFlow(false)
    private val clients = mutableSetOf<WebSocket>()
    val players = MutableStateFlow<MutableMap<WebSocket?, LanUserEntity?>>(mutableMapOf())

    private val _serverEventState = MutableSharedFlow<Event>(
        replay = 0, extraBufferCapacity = 10
    )
    val serverEventState: MutableSharedFlow<Event> get() = _serverEventState

    val idCounter = MutableStateFlow(1)
    val roundCounter = MutableStateFlow(0)
    val tablesCards = MutableStateFlow<MutableList<Card>>(mutableListOf())
    val baseTable = MutableStateFlow<Card?>(null)
    var cardsUnderTest = MutableStateFlow<MutableList<Card>>(mutableListOf())
    val lastPlayedCards = MutableStateFlow<MutableList<Card>>(mutableListOf())
    val lastThrowerId = MutableStateFlow(0)
    val currentTurnId = MutableStateFlow(0)
    val botVoteYesCount = MutableStateFlow(0)
    val botVoteActive = MutableStateFlow(false)

    private var listOfCards = mutableListOf<Card>()
    var serverHandler: ServerHandler? = null
    private var onStarted: (() -> Unit)? = null

    // ✅ Host always uses a dedicated sentinel key — never null
    private val HOST_KEY: WebSocket = FakeBotSocket()
    private val hostConn get() = HOST_KEY
    private val hostPlayer get() = players.value[HOST_KEY]

    private val botNames = listOf("Bot 1", "Bot 2", "Bot 3")
    private val botImages = listOf(
        "https://robohash.org/bot1?set=set2",
        "https://robohash.org/bot2?set=set2",
        "https://robohash.org/bot3?set=set2"
    )
    val disconnectedPlayers = mutableMapOf<String, Pair<WebSocket?, LanUserEntity>>()

    fun setOnStartedListener(listener: () -> Unit) {
        onStarted = listener
    }

    init {
        listOfCards = listOfCard.toMutableList()
        serverHandler = ServerHandler(
            server = this,
            broadcast = { event -> broadcastEvent(event) },
            dealCards = {}
        )
    }

    override fun onOpen(conn: WebSocket, handshake: ClientHandshake?) {
        println("✅ New connection: ${conn.remoteSocketAddress}")
        clients.add(conn)
    }

    override fun onClose(conn: WebSocket, code: Int, reason: String?, remote: Boolean) {
        println("❌ Disconnected: ${conn.remoteSocketAddress}")
        clients.remove(conn)

        val disconnectedPlayer = players.value[conn] ?: return

        if (disconnectedPlayer.isHost || disconnectedPlayer.isBot) {
            players.value = players.value.toMutableMap().apply { remove(conn) }
            return
        }

        val deviceId = disconnectedPlayer.deviceId
        if (deviceId.isNotEmpty()) {
            disconnectedPlayers[deviceId] = Pair(conn, disconnectedPlayer)
        }

        val botIndex = disconnectedPlayers.size - 1
        val bot = disconnectedPlayer.copy(
            name = botNames.getOrElse(botIndex % 3) { "Bot" },
            image = botImages.getOrElse(botIndex % 3) { botImages[0] },
            isBot = true,
            isDisconnected = false
        )

        val botSocket = FakeBotSocket()
        players.value = players.value.toMutableMap().apply {
            remove(conn)
            put(botSocket, bot)
        }

        broadcastEvent(
            RoomStateEvent(
                playersInRoom = players.value.values.toMutableList(),
                baseTable = baseTable.value,
                tablesCards = tablesCards.value,
                round = roundCounter.value,
                currentTurnId = currentTurnId.value
            )
        )

        if (currentTurnId.value == bot.id) {
            scheduleBotTurn(bot)
        }
    }

    override fun onMessage(conn: WebSocket, message: String?) {
        if (message == null) return
        println("📩 Message from ${conn.remoteSocketAddress}: $message")
        try {
            val event = Json.decodeFromString(Event.serializer(), message)
            serverHandler?.handle(conn = conn, event)
        } catch (e: Exception) {
            println("server -> ❌ Failed to parse event: ${e.message}")
        }
    }

    override fun onError(conn: WebSocket?, ex: Exception?) {
        println("❗ WebSocket error: ${ex?.message}")
    }

    override fun onStart() {
        println("✅ WebSocket server started on port $port")
        onStarted?.invoke()
    }

    // ─── Host actions ──────────────────────────────────────────────────────────

    fun hostPlayCard(listOfCard: List<Card>) {
        val currentHostPlayer = hostPlayer ?: return

        val remainingCards = currentHostPlayer.cards.filterNot { it in listOfCard }
        val updatedHost = currentHostPlayer.copy(cards = remainingCards)

        players.value = players.value.toMutableMap().apply {
            put(HOST_KEY, updatedHost)
        }

        tablesCards.value = (tablesCards.value + listOfCard).toMutableList()
        cardsUnderTest.value = (cardsUnderTest.value + listOfCard).toMutableList()
        lastPlayedCards.value = listOfCard.toMutableList()
        lastThrowerId.value = currentHostPlayer.id

        val nextId = FunctionHelper.nextPlayer(players, currentHostPlayer.id)
        currentTurnId.value = nextId

        val nextPlayer = players.value.values.find { it?.id == nextId }
        if (nextPlayer?.isBot == true) scheduleBotTurn(nextPlayer)

        broadcastEvent(
            CardPlayEvent(
                card = listOfCard,
                playerId = currentHostPlayer.id,
                nextPlayer = nextId,
                index = listOfCard.size
            )
        )

        broadcastEvent(
            RoomStateEvent(
                playersInRoom = players.value.values.toMutableList(),
                round = roundCounter.value,
                tablesCards = tablesCards.value,
                baseTable = baseTable.value,
                currentTurnId = nextId
            )
        )
    }

    fun hostLiarCall() {
        broadcastEvent(LiarCallEvent(callerId = hostPlayer?.id ?: 1))
        roundStarted.value = false
    }

    fun hostLiarCallProcess() {
        try {
            val currentHostPlayer = hostPlayer ?: return

            val hasWrongCard =
                lastPlayedCards.value.any { it.rank != baseTable.value?.rank && it.rank != Rank.Joker }

            val callerId = currentHostPlayer.id
            val throwerId = lastThrowerId.value
            val loserId = if (hasWrongCard) throwerId else callerId

            val loser = players.value.values.find { it?.id == loserId }
            val loserConn = players.value.entries.find { it.value?.id == loserId }?.key

            if (loser == null) return

            val isRealBullet = loser.remainingBullets == loser.numOfShot

            val updatedLoser = if (isRealBullet) {
                loser.copy(remainingBullets = 0, isAlive = false)
            } else {
                loser.copy(remainingBullets = (loser.remainingBullets - 1).coerceAtLeast(0))
            }

            players.value = players.value.toMutableMap().apply {
                put(loserConn, updatedLoser)
            }

            broadcastEvent(
                LiarCallResult(
                    loserId = loserId,
                    isRealBullet = isRealBullet,
                    cardsRank = lastPlayedCards.value
                )
            )

            cardsUnderTest.value = mutableListOf()
            lastPlayedCards.value = mutableListOf()
            lastThrowerId.value = 0
            roundStarted.value = false

            val alivePlayers = players.value.values.filter { it?.isAlive == true }
            if (alivePlayers.size == 1) {
                val winner = alivePlayers.first()
                broadcastEvent(
                    GameOverEvent(winnerId = winner?.id ?: 0, winnerName = winner?.name ?: "")
                )
            }
        } catch (e: Exception) {
            Log.e("hostLiarCallProcess", "error: ${e.message}")
        }
    }

    fun hostPlayerWarning() {
        broadcastEvent(WarningEvent)
    }

    fun startNewRound() {
        if (players.value.isEmpty()) return
        roundStarted.value = false
        Log.d("startNewRound", "players=${players.value.values.size}")
        dealCards()
    }

    fun addHostPlayer(playerName: String) {
        val host = LanUserEntity(
            name = playerName,
            numOfShot = (1..6).random(),
            remainingBullets = 6,
            isAlive = true,
            image = "https://robohash.org/${playerName}?set=set5",
            id = 1,
            ipAddress = getMyIpAddress(),
            cards = mutableListOf(),
            isHost = true
        )
        players.value = players.value.toMutableMap().apply {
            put(HOST_KEY, host)
        }
        println("🎯 Host player added: $playerName")
        printPlayers()
    }

    fun hostPlayAgain() {
        val resetPlayers = players.value.toMutableMap()
        resetPlayers.forEach { (conn, player) ->
            resetPlayers[conn] = player?.copy(
                remainingBullets = 6,
                isAlive = true,
                numOfShot = (1..6).random(),
                cards = mutableListOf()
            )
        }
        players.value = resetPlayers

        roundStarted.value = false
        roundCounter.value = 0
        tablesCards.value = mutableListOf()
        cardsUnderTest.value = mutableListOf()
        lastPlayedCards.value = mutableListOf()
        lastThrowerId.value = 0
        baseTable.value = null
        idCounter.value = players.value.size

        broadcastEvent(PlayAgainEvent)
        dealCards()
    }

    // ─── Internal ──────────────────────────────────────────────────────────────

    fun dealCards() {
        Log.d("dealCards", "CALLED — roundStarted=${roundStarted.value}")
        if (roundStarted.value) {
            Log.d("dealCards", "SKIPPED")
            return
        }
        roundStarted.value = true
        if (players.value.isEmpty()) return

        listOfCards = listOfCard.shuffled().toMutableList()

        var updatedPlayers = players.value.toMutableMap()
        updatedPlayers.keys.forEach { conn ->
            val player = updatedPlayers[conn] ?: return@forEach
            val hand = listOfCards.take(5)
            listOfCards = listOfCards.drop(5).toMutableList()
            updatedPlayers = updatedPlayers.toMutableMap().apply {
                put(conn, player.copy(cards = hand))
            }
        }
        players.value = updatedPlayers

        baseTable.value = listOfCards.filter { it.rank != Rank.Joker }.random()
        roundCounter.value += 1
        tablesCards.value = mutableListOf()
        cardsUnderTest.value = mutableListOf()
        lastPlayedCards.value = mutableListOf()
        lastThrowerId.value = 0

        val sortedAliveIds =
            players.value.values.filterNotNull().filter { it.isAlive }.map { it.id }.sorted()

        if (sortedAliveIds.isNotEmpty()) {
            val startIndex = (roundCounter.value - 1) % sortedAliveIds.size
            currentTurnId.value = sortedAliveIds[startIndex]
        }

        broadcastEvent(
            RoomStateEvent(
                playersInRoom = players.value.values.toMutableList(),
                tablesCards = tablesCards.value,
                baseTable = baseTable.value,
                round = roundCounter.value,
                currentTurnId = currentTurnId.value
            )
        )

        val currentPlayer = players.value.values.find { it?.id == currentTurnId.value }
        if (currentPlayer?.isBot == true) scheduleBotTurn(currentPlayer)
    }

    private fun <T : Event> broadcastEvent(event: T) {
        val json = Json.encodeToString(Event.serializer(), event)
        clients.toList().forEach { client ->
            try {
                client.send(json)
            } catch (e: Exception) {
                println("❌ Failed to send to ${client.remoteSocketAddress}: ${e.message}")
            }
        }
        _serverEventState.tryEmit(event)
    }

    fun printPlayers() {
        println("📋 Current players:")
        players.value.forEach { (conn, player) ->
            println("  - ${player?.name} (conn=${conn?.hashCode() ?: "HOST"})")
        }
        println("📊 Total: ${players.value.size} players")
    }

    fun scheduleBotTurn(bot: LanUserEntity) {
        Thread {
            Thread.sleep((2000..3000).random().toLong())
            val currentBot = players.value.values.find { it?.id == bot.id }
            if (currentBot?.isBot == true && currentTurnId.value == bot.id) {
                executeBotTurn(bot)
            }
        }.start()
    }

    private fun executeBotTurn(bot: LanUserEntity) {
        val currentBot = players.value.values.find { it?.id == bot.id } ?: return
        if (currentTurnId.value != currentBot.id) return
        if (currentBot.cards.isEmpty()) return

        // ✅ Bot liar call logic:
        // Bot calls liar if there are cards on the table AND it's suspicious enough.
        // Suspicion increases the more cards are on the table (more chances of a bluff).
        val cardsOnTable = tablesCards.value.size
        val hasCardsOnTable = lastPlayedCards.value.isNotEmpty()

        // ✅ Bot decides to call liar based on:
        // - 20% base chance
        // - +10% for every 2 cards already on the table (more cards = more bluffs likely)
        val liarCallChance = if (hasCardsOnTable) {
            20 + (cardsOnTable / 2) * 10
        } else {
            0 // can't call liar if no cards thrown yet
        }.coerceAtMost(60) // cap at 60% so bots don't always call liar

        val shouldCallLiar = hasCardsOnTable && (1..100).random() <= liarCallChance

        if (shouldCallLiar) {
            // ✅ Bot calls liar
            broadcastEvent(LiarCallEvent(callerId = currentBot.id))
            // Process liar call result directly on server
            Thread.sleep(1200)
            processBotLiarCall(currentBot)
        } else {
            // Bot plays cards
            val shouldBluff = (1..10).random() <= 3
            val cardCount = (1..minOf(3, currentBot.cards.size)).random()

            val cardsToPlay = if (shouldBluff) {
                currentBot.cards.shuffled().take(cardCount)
            } else {
                val correctCards = currentBot.cards.filter {
                    it.rank == baseTable.value?.rank || it.rank == Rank.Joker
                }
                if (correctCards.isEmpty()) currentBot.cards.shuffled().take(cardCount)
                else correctCards.take(minOf(cardCount, correctCards.size))
            }

            val botConn = players.value.entries.find { it.value?.id == bot.id }?.key
            serverHandler?.handle(
                botConn,
                CardPlayEvent(
                    playerId = currentBot.id,
                    card = cardsToPlay,
                    nextPlayer = 0,
                    index = cardsToPlay.size
                )
            )
        }
    }

    // ✅ Processes liar call result when a bot is the caller
    private fun processBotLiarCall(callerBot: LanUserEntity) {
        val hasWrongCard = lastPlayedCards.value
            .any { it.rank != baseTable.value?.rank && it.rank != Rank.Joker }

        val throwerId = lastThrowerId.value
        val loserId = if (hasWrongCard) throwerId else callerBot.id

        val loser = players.value.values.find { it?.id == loserId }
        val loserConn = players.value.entries.find { it.value?.id == loserId }?.key

        if (loser == null) return

        val isRealBullet = loser.remainingBullets == loser.numOfShot
        val updatedLoser = if (isRealBullet) {
            loser.copy(remainingBullets = 0, isAlive = false)
        } else {
            loser.copy(remainingBullets = (loser.remainingBullets - 1).coerceAtLeast(0))
        }

        players.value = players.value.toMutableMap().apply {
            put(loserConn, updatedLoser)
        }

        broadcastEvent(
            LiarCallResult(
                loserId = loserId,
                isRealBullet = isRealBullet,
                cardsRank = lastPlayedCards.value
            )
        )

        cardsUnderTest.value = mutableListOf()
        lastPlayedCards.value = mutableListOf()
        lastThrowerId.value = 0
        roundStarted.value = false

        val alivePlayers = players.value.values.filter { it?.isAlive == true }
        if (alivePlayers.size == 1) {
            val winner = alivePlayers.first()
            broadcastEvent(GameOverEvent(winnerId = winner?.id ?: 0, winnerName = winner?.name ?: ""))
        }
    }

    fun addBotsToFill() {
        val currentPlayers = players.value.values.filterNotNull()
        val botsNeeded = 4 - currentPlayers.size

        if (botsNeeded <= 0) {
            startNewRound()
            return
        }

        val updatedPlayers = players.value.toMutableMap()
        for (i in 0 until botsNeeded) {
            val botId = (players.value.values.filterNotNull().maxOfOrNull { it.id } ?: 0) + i + 1
            val bot = LanUserEntity(
                id = botId,
                name = "Bot $botId",
                image = "https://robohash.org/bot$botId?set=set2",
                isAlive = true,
                numOfShot = (1..6).random(),
                remainingBullets = 6,
                cards = mutableListOf(),
                isHost = false,
                isBot = true,
                deviceId = "bot_$botId",
                ipAddress = "127.0.0.$botId"
            )
            updatedPlayers[FakeBotSocket()] = bot
        }

        players.value = updatedPlayers

        broadcastEvent(
            RoomStateEvent(
                playersInRoom = players.value.values.toMutableList(),
                baseTable = baseTable.value,
                tablesCards = tablesCards.value,
                round = roundCounter.value,
                currentTurnId = currentTurnId.value
            )
        )

        Thread {
            Thread.sleep(1500)
            startNewRound()
        }.start()
    }

    fun broadcastBotVoteDialog(totalPlayers: Int) {
        broadcastEvent(
            BotVoteStateEvent(
                yesCount = 0,
                totalPlayers = totalPlayers,
                showDialog = true
            )
        )
    }
}