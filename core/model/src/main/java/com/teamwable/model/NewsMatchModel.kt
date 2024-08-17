package com.teamwable.model

data class NewsMatchModel(
    val date: String,
    val games: List<NewsMatchScoreModel>,
)
