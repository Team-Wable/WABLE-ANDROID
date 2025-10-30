package com.teamwable.network.dto.response.news

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseScheduleDto(
    @SerialName("date")
    val date: String = "",
    @SerialName("games")
    val games: List<ResponseGameDto>,
)

@Serializable
data class ResponseGameDto(
    @SerialName("gameDate")
    val gameDate: String = "",
    @SerialName("aTeamName")
    val aTeamName: String = "",
    @SerialName("aTeamScore")
    val aTeamScore: Int = 0,
    @SerialName("bTeamName")
    val bTeamName: String = "",
    @SerialName("bTeamScore")
    val bTeamScore: Int = 0,
    @SerialName("gameStatus")
    val gameStatus: String = "",
)
