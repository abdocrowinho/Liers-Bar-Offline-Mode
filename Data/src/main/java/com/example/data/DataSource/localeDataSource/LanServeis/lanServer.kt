 package com.example.data.DataSource.localeDataSource.LanServeis
import android.util.Log
import com.example.domain.Entitys.Card
import com.example.domain.Entitys.LanUserEntity
import com.example.domain.Entitys.Rank
import com.example.domain.GameEvents.CardPlayEvent
import com.example.domain.GameEvents.Event
import com.example.domain.GameEvents.JoinToGameEvent
import com.example.domain.GameEvents.RoomStateEvent
import com.example.domain.GameEvents.StartRoundEvent
import com.example.domain.Utlites.Constant.listOfCard
import com.example.domain.Utlites.getMyIpAddress
import createFakeConnection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import java.net.InetSocketAddress

 class GameWebSocketServer(port: Int) : WebSocketServer(InetSocketAddress(port)) {
     private var roundStarted = false
     private val clients = mutableSetOf<WebSocket>()
     val players =MutableStateFlow< MutableMap<WebSocket?, LanUserEntity>>(mutableMapOf())

   private  val _serverEventState = MutableStateFlow<Event?>(null)
     val serverEventState: StateFlow<Event?> = _serverEventState.asStateFlow()

     val idCounter = MutableStateFlow(1)
     val  roundCounter = MutableStateFlow(1)
     val  tablesCards= MutableStateFlow< MutableList<Card>>(mutableListOf())
     val baseTable= MutableStateFlow<Card?>(null)
     private var listOfCards = mutableListOf(Card(rank = Rank.ACE, imageCard = 0, "", 1))
     private var serverHandler: ServerHandler? = null
     private var onStarted: (() -> Unit)? = null

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
                 dealCards()
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




     fun hostPlayCard(listOfCard:List<Card>){
         // update cards in host hand
        val hostConn =  players.value.keys.filterNot { it in clients }.firstOrNull()
         val hostPlayer = players.value[hostConn]

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
                 round = 0,
                 tablesCards = tablesCards.value,
                 tableBase =baseTable.value
             ),
             )

     }


     private fun dealCards() {
         if (roundStarted) return
         roundStarted = true
         if (players.value.isNotEmpty()) {
             this.listOfCards = this.listOfCards.shuffled().toMutableList()

            // update player with take new cards

             players.value.keys.forEach { conn ->
                 val player = players.value[conn]
                 val hand = listOfCards.take(5)
                 listOfCards.removeAll(hand)

                 if (player != null) {
                     val updatePlayer = player.copy(cards = hand)
             players.value =    players.value.toMutableMap().apply {
                     put(conn,updatePlayer)
                 }
                 }

             }
             baseTable.value= listOfCards.random()

             //send to clients new event

             val allPlayers = players.value.values.map {
                 JoinToGameEvent(it)
             }
             val event = RoomStateEvent(allPlayers.map {
                 it.player

             }, tablesCards = tablesCards.value,
                 tableBase = baseTable.value, round = roundCounter.value)

             broadcastEvent(event)

//             val json = Json.encodeToString(Event.serializer(), event)
//             players.value.keys.forEach { conn ->
//                 conn?.send(json)
//             }
         }
     }

     fun addHostPlayer(playerName: String) {
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
         dealCards()
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
         _serverEventState.value = event
     }


     fun printPlayers() {
         println("📋 Current players in server:")
         players.value.forEach { (conn, player) ->
             println("   - ${player.name} (Connection: ${conn?.hashCode()})")
         }
         println("📊 Total: ${players.value.size} players")
     }


 }