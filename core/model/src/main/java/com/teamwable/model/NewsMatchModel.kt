package com.teamwable.model

data class NewsMatchModel(
    val gameDate: String,
    val aTeamName: String,
    val aTeamScore: Int,
    val bTeamName: String,
    val bTeamScore: Int,
    val gameStatus: String,
)
