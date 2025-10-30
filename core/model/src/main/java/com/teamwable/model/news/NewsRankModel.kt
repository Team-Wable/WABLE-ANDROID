package com.teamwable.model.news

data class NewsRankModel(
    val teamRank: Int,
    val teamName: String,
    val teamWin: Int,
    val teamDefeat: Int,
    val winningRate: Int,
    val scoreDiff: Int,
)
