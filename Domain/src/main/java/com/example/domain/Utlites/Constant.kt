package com.example.domain.Utlites

import com.example.domain.Entitys.Card
import com.example.domain.Entitys.Rank
import com.example.domain.R


fun createCads() : List<Card>{
     val cardsList = mutableListOf<Card>()
    for(i in 1..27){
      if (i<=6){
         val card = Card(rank = Rank.ACE, imageCard = R.drawable.ace, colorHex = "",i)
         cardsList.add(card)
      }else if (i in 7..12){
         val card = Card(rank = Rank.QUEEN, imageCard = R.drawable.queen, colorHex = "",i)
         cardsList.add(card)
      }else if (i in 13..18){
         val card = Card(rank = Rank.KING, imageCard = R.drawable.king, colorHex = "",i)
         cardsList.add(card)
      }else if (i in 19..24){
         val card = Card(rank = Rank.JACK, imageCard = R.drawable.jack, colorHex = "",i)
         cardsList.add(card)
      }else {
         val card = Card(rank = Rank.Joker, imageCard = R.drawable.joker, colorHex = "",i)
         cardsList.add(card)
      }
      }
    return cardsList
}

object Constant {
    val listOfCard = createCads()
}