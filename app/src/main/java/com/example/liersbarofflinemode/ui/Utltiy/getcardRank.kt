package com.example.liersbarofflinemode.ui.Utltiy

import com.example.domain.Entitys.Rank

fun getCardRank(rank: Rank?): Int? {
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