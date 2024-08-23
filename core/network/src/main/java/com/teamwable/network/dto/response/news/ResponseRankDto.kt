package com.teamwable.network.dto.response.news

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseRankDto(
    @SerialName("teamRank")
    val teamRank: Int = 0,
    @SerialName("teamName")
    val teamName: String = "",
    @SerialName("teamWin")
    val teamWin: Int = 0,
    @SerialName("teamDefeat")
    val teamDefeat: Int = 0,
    @SerialName("winningRate")
    val winningRate: Int = 0,
    @SerialName("scoreDiff")
    val scoreDiff: Int = 0,
)
