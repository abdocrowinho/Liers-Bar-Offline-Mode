package com.example.liersbarofflinemode.ui.States
import com.example.domain.Entitys.Card
import com.example.domain.Entitys.LanUserEntity
import com.example.domain.Entitys.Rank
data class TablePlayersState(
    var showCardsState : Boolean=false,
    var cardCountState :Int?=0,
    var cardPlayerIdState :Int?=-1,
    var gunSound : Int?=0,
    var isFindCardsInTable : Boolean=false,
    var isWarning :Boolean=false,
    var isMyTurn :Boolean=false,
    var tableBase : Rank?=null,
    var roundCounter : Int?=0,
    val cardsUnderTest:List<Card?>?= mutableListOf(),
    val spokenText:String?="",
    val isPlayerCallingLiar :Boolean?=false,
    val isRealBullet:Boolean?=null,
    val looser:LanUserEntity?=null,
    val bulletWordState :String?="",
    val isGameOver: Boolean = false,
    val winnerName: String = "",
    val winnerId: Int = -1,
    val currentTurnId: Int = 0,
    val bulletsBeforeShot: Int = 6,
    val showBotVoteDialog: Boolean = false,
    val botVoteYesCount: Int = 0,
    val botVoteTotalPlayers: Int = 0,
    val botVoteRejected: Boolean = false


)