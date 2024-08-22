package com.teamwable.model.news

data class NewsMatchModel(
    val date: String,
    val games: List<NewsMatchScoreModel>,
)
