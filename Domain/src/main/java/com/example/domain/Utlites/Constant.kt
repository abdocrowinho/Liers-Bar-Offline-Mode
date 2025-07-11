package com.example.data.DataSource.Utltity

import com.example.domain.Entitys.Card
import com.example.domain.Entitys.Rank

object Constant {
    private val jackCardsList = mutableListOf<Card>()
    for(i in 1..6){
      val card = Card(rank = Rank.ACE, imageCard = 4, colorHex = "")
      jackCardsList.add(card)
   }

    val listOfCard = mutableListOf(

        Card(rank = Rank.ACE, imageCard = 4, colorHex = ""),
        Card(rank = Rank.ACE, imageCard = "", colorHex = ""),
        Card(rank = Rank.ACE, imageCard = "", colorHex = ""),
        Card(rank = Rank.ACE, imageCard = "", colorHex = ""),
        Card(rank = Rank.ACE, imageCard = "", colorHex = ""),
        Card(rank = Rank.ACE, imageCard = "", colorHex = ""),
        //Jack
        Card(rank = Rank.JACK, imageCard = "", colorHex = ""),
        Card(rank = Rank.JACK, imageCard = "", colorHex = ""),
        Card(rank = Rank.JACK, imageCard = "", colorHex = ""),
        Card(rank = Rank.JACK, imageCard = "", colorHex = ""),
        Card(rank = Rank.JACK, imageCard = "", colorHex = ""),
        Card(rank = Rank.JACK, imageCard = "", colorHex = ""),
        //Queen
        Card(rank = Rank.QUEEN, imageCard = "", colorHex = ""),
        Card(rank = Rank.QUEEN, imageCard = "", colorHex = ""),
        Card(rank = Rank.QUEEN, imageCard = "", colorHex = ""),
        Card(rank = Rank.QUEEN, imageCard = "", colorHex = ""),
        Card(rank = Rank.QUEEN, imageCard = "", colorHex = ""),
        Card(rank = Rank.QUEEN, imageCard = "", colorHex = ""),
        //King
        Card(rank = Rank.KING, imageCard = "", colorHex = ""),
        Card(rank = Rank.KING, imageCard = "", colorHex = ""),
        Card(rank = Rank.KING, imageCard = "", colorHex = ""),
        Card(rank = Rank.KING, imageCard = "", colorHex = ""),
        Card(rank = Rank.KING, imageCard = "", colorHex = ""),
        Card(rank = Rank.KING, imageCard = "", colorHex = ""),
        // Joker
        Card(rank = Rank.Joker, imageCard = "", colorHex = ""),
        Card(rank = Rank.Joker, imageCard = "", colorHex = ""),
        Card(rank = Rank.Joker, imageCard = "", colorHex = ""),

        ).toMutableList()
}