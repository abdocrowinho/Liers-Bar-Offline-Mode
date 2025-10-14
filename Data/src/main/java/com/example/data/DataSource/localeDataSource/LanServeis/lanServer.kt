 package com.example.data.DataSource.localeDataSource.LanServeis
import android.util.Log
import com.example.data.DataSource.Utltity.FunctionHelper
import com.example.domain.Entitys.Card
import com.example.domain.Entitys.LanUserEntity
import com.example.domain.Entitys.Rank
import com.example.domain.GameEvents.CardPlayEvent
import com.example.domain.GameEvents.Event
import com.example.domain.GameEvents.JoinToGameEvent
import com.example.domain.GameEvents.LiarCallEvent
import com.example.domain.GameEvents.LiarCallResult
import com.example.domain.GameEvents.RoomStateEvent
import com.example.domain.GameEvents.WarningEvent
import com.example.domain.Utlites.Constant.listOfCard
import com.example.domain.Utlites.getMyIpAddress
import createFakeConnection
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import java.net.InetSocketAddress

 class GameWebSocketServer(port: Int) : WebSocketServer(InetSocketAddress(port)) {
      var roundStarted =  MutableStateFlow(false)
     private val clients = mutableSetOf<WebSocket>()
     val players =MutableStateFlow< MutableMap<WebSocket?, LanUserEntity?>>(mutableMapOf())

   private  val _serverEventState = MutableStateFlow<Event?>(null)
     val serverEventState: StateFlow<Event?> = _serverEventState.asStateFlow()

     val idCounter = MutableStateFlow(1)
     val  roundCounter = MutableStateFlow(0)
     val  tablesCards= MutableStateFlow< MutableList<Card>>(mutableListOf())
     val baseTable= MutableStateFlow<Card?>(null)
     private var listOfCards = mutableListOf(Card(rank = Rank.ACE, imageCard = 0, "", 1))
     private var serverHandler: ServerHandler? = null
     private var onStarted: (() -> Unit)? = null
     var cardsUnderTest = MutableStateFlow<MutableList<Card>>(mutableListOf())

     fun setOnStartedListener(listener: () -> Unit) {
         onStarted = listener
     }

     init {
         listOfCards = listOfCard.toMutableList()
         serverHandler = ServerHandler(
             server = this,
             broadcast = { event ->
                 broadcastEvent(event)
             },
             dealCards = {
             }
         )
     }

     override fun onOpen(conn: WebSocket, handshake: ClientHandshake?) {
         println("✅ New connection: ${conn.remoteSocketAddress}")
         clients.add(conn)
     }

     override fun onClose(conn: WebSocket, code: Int, reason: String?, remote: Boolean) {
         println("❌ Disconnected: ${conn.remoteSocketAddress}")
         clients.remove(conn)
         players.value = players.value.toMutableMap().apply {
            remove(conn)
         }
     }

     override fun onMessage(conn: WebSocket, message: String?) {
         if (message == null) return

         println("📩 Message from ${conn.remoteSocketAddress}: $message")

         val event = Json.decodeFromString(Event.serializer(), message)

         try {
             serverHandler?.handle(conn = conn, event)


         } catch (e: Exception) {
             println("server ->  ❌ Failed to parse event: ${e.message}")
         }
     }

     override fun onError(conn: WebSocket?, ex: Exception?) {
         println("❗ WebSocket error: ${ex?.message}")
     }
     override fun onStart() {
         onStarted?.invoke()
         println("step WebSocket server started on port $port")
     }
     private val hostConn =  players.value.keys.filterNot { it in clients }.firstOrNull()
     private val hostPlayer = players.value[hostConn]


fun hostLiarCallProcess(){
     val hasWrongCard = cardsUnderTest.value.any { it.rank != baseTable.value!!.rank&&it.rank!=Rank.Joker }
     val loserId = if (hasWrongCard) {
        FunctionHelper.afterPlayer(players, hostPlayer?.id ?: 1)
    } else {
        hostPlayer?.id ?: 1
    }
     val loser = players.value.values.find { it?.id == loserId }
    val isRealBullet = (loser?.remainingBullets == loser?.numOfShot)

    broadcastEvent(
        LiarCallResult(
            loserId = loserId,
            isRealBullet = isRealBullet,
            cardsRank = cardsUnderTest.value
        )
    )
     cardsUnderTest.value =  mutableListOf()
}

     fun hostLiarCall() {
         broadcastEvent(LiarCallEvent(callerId = hostPlayer?.id ?: 1))
         roundStarted.value=false

     }

     fun updateLooserPlayer(isRealBullet:Boolean,){
         Log.d("loser player updating in server", "")
          val hasWrongCard = cardsUnderTest.value.any { it.rank != baseTable.value!!.rank }
          val loserId = if (hasWrongCard) {
             FunctionHelper.afterPlayer(players, hostPlayer?.id ?: 1)
         } else {
             hostPlayer?.id ?: 1
         }
          val loser = players.value.values.find { it?.id == loserId }
          val loserConn = players.value.keys.find { players.value[it]?.id == loserId }
         val updateLoserPlayer: LanUserEntity? = if (isRealBullet) {
             loser?.copy(remainingBullets = 0, isAlive = false)
         } else {
             loser?.copy(remainingBullets = loser.remainingBullets.minus(1))
         }
         Log.d("loser player updating in server", updateLoserPlayer?.remainingBullets.toString())

         players.value = players.value.apply {
             put(loserConn, updateLoserPlayer)
         }
     }

fun hostPlayerWarning(){
    val event = WarningEvent
    broadcastEvent(event)
}

     fun hostPlayCard(listOfCard:List<Card>){
          val hostConn =  players.value.keys.filterNot { it in clients }.firstOrNull()
          val hostPlayer = players.value[hostConn]

         // update cards in host hand
         cardsUnderTest.value = cardsUnderTest.value.apply {
        addAll( mutableListOf())
         }


         println("get host player debug : ${hostPlayer?.name}")

         val newCards = hostPlayer?.cards?.filterNot { card -> card in listOfCard }

         println("cards count after play card : ${newCards?.size}")

         val newPlayer = hostPlayer?.copy(cards = newCards?: listOf())


         if (hostPlayer!=null&&newPlayer!=null){
        players.value =     players.value.toMutableMap().apply {
                 put(hostConn,newPlayer)
             }
             Log.d("card", "hostPlayCard: player play card")
         }

         println("player's cards count : ${players.value[hostConn]?.cards}")

         // update table
      tablesCards.value = tablesCards.value.apply {
          addAll(listOfCard)
      }

         broadcastEvent(CardPlayEvent(card = listOfCard,
             playerId =hostPlayer?.id?:1, nextPlayer = 2, index = listOfCard.size))

         broadcastEvent(
             RoomStateEvent(
                 playersInRoom = players.value.values.toMutableList(),
                 round = roundCounter.value,
                 tablesCards = tablesCards.value,
                 tableBase =baseTable.value
             ),
             )
         cardsUnderTest.value = cardsUnderTest.value.apply {
            addAll(listOfCard.toMutableList())
         }
     }

       fun startNewRound(){
          if (players.value.values.size==1||players.value.isNotEmpty()){
   Log.d("players when new round called",players.value.values.size.toString())

              dealCards()
             Log.d("Start New Round: ", "Round Started successfully ")
         }
     }


     private fun dealCards() {
        if (roundStarted.value) return
         roundStarted.value = true
         if (players.value.isNotEmpty()) {
             this.listOfCards = this.listOfCards.shuffled().toMutableList()

            // update player with take new cards

             players.value.keys.forEach { conn ->
                 val player = players.value[conn]
                 val hand = listOfCards.take(5)
                 listOfCards.removeAll(hand)

                 if (player != null) {
                     val updatePlayer = player.copy(cards = hand)
             players.value = players.value.toMutableMap().apply {
                     put(conn,updatePlayer)
                 }
                 }

             }
             baseTable.value = listOfCards.filter {
                 it.rank!=Rank.Joker
             }.random()

             //send to clients new event

             val allPlayers = players.value.values.map {
                 it?.let { it1 -> JoinToGameEvent(it1) }
             }
             roundCounter.value += 1

             val event = RoomStateEvent(
                 allPlayers.map { it?.player },
                 tablesCards = tablesCards.value,
                 tableBase = baseTable.value,
                 round = roundCounter.value
             )

             broadcastEvent(event)

//             val json = Json.encodeToString(Event.serializer(), event)
//             players.value.keys.forEach { conn ->
//                 conn?.send(json)
//             }
         }
     }

    suspend fun addHostPlayer(playerName: String) {
         val hostPlayer = LanUserEntity(
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
         val fakeConnection = createFakeConnection(port)
         players.value = players.value.toMutableMap().apply {
             put(fakeConnection,hostPlayer)
         }
         println("🎯 Host player added directly: $playerName")
         printPlayers()
         delay(2000)
     }

     private fun<T:Event>broadcastEvent(event: T) {
         val json = Json.encodeToString(Event.serializer(), event)
         val snapshot = clients.toList()
         snapshot.forEach { client ->
             try {
                 client.send(json)
             } catch (e: Exception) {
                 println("❌ Failed to send to ${client.remoteSocketAddress}: ${e.message}")
             }
         }
         _serverEventState.tryEmit(event)
     }


     fun printPlayers() {
         println("📋 Current players in server:")
         players.value.forEach { (conn, player) ->
             println(" ay 7aga  - ${player?.name} (Connection: ${conn?.hashCode()})")
         }
         println("📊 Total: ${players.value.size} players")
     }


 }