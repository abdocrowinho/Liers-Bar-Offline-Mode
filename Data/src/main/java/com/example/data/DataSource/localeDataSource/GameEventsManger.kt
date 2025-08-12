//package com.example.data.DataSource.localeDataSource
//
//import com.example.domain.Entitys.Card
//import kotlinx.serialization.encodeToString
//import kotlinx.serialization.json.Json
//
//class GameEventsManger {
//
//    private val playersCards = mutableMapOf<String, MutableList<Card>>()
//    private val playersInRoom = mutableListOf<JoinedGameEvent>()
//
//    fun applyEvent(event: GameEvent): List<GameEvent> {
//        return when (event.type) {
//            "CARD_PLAYED" -> {
//                val cardPlay = Json.decodeFromString<CardPlayEvent>(event.data)
//                handleCardPlayed(cardPlay)
//            }
//
//            "LIAR_CALLED" -> {
//                val liarCall = Json.decodeFromString<LiarCallEvent>(event.data)
//                handleLiarCallEvent(liarCall)
//            }
//
//            "PLAYER_SHOT" -> {
//                val shot = Json.decodeFromString<PlayerShotEvent>(event.data)
//                handlePlayerShot(shot)
//            }
//            "PLAYER_JOINED" -> {
//                val joinEvent = Json.decodeFromString<JoinedGameEvent>(event.data)
//                handlePlayerJoined(joinEvent)
//            }
//
//            else -> emptyList()
//        }
//
//
//    }
//
//    private fun handleCardPlayed(event: CardPlayEvent): List<GameEvent> {
//        playersCards[event.playerId]?.remove(event.card)
//        return listOf(GameEvent(type = "Brodcast Play card ", data = Json.encodeToString(event)))
//    }
//
//    private fun handleLiarCallEvent(event: LiarCallEvent): List<GameEvent> {
//
//        val accusedCard = playersCards[event.accusedId]?.lastOrNull()
//
//        val loser = if (accusedCard?.rank != event.cardTable.rank) {
//            event.accusedId
//        } else {
//            event.callerId
//        }
//        return listOf(GameEvent(type = "liar result ", data = Json.encodeToString(event)))
//
//    }
//
//    private fun handlePlayerShot(event: PlayerShotEvent): List<GameEvent> {
//
//        return listOf(
//            GameEvent(type = "PLAYER_SHOT_RESULT", data = Json.encodeToString(event))
//        )
//    }
//
//    private fun handlePlayerJoined(event: JoinedGameEvent): List<GameEvent> {
//        playersInRoom.add(event)
//
//        val notifyOthers = GameEvent(
//            type = "player Joined", data =
//            Json.encodeToString(event)
//        )
//        val sendCurrentRoomState = GameEvent(
//            type = "ROOM_STATE",
//            data = Json.encodeToString(RoomStateEvent(playersInRoom))
//        )
//        return listOf(notifyOthers, sendCurrentRoomState)
//    }
//
//    fun setInitialCards(playerId: String, cards: List<Card>) {
//        playersCards[playerId] = cards.toMutableList()
//    }
//}
//
