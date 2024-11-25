package com.teamwable.data.mapper.toModel

import com.teamwable.model.news.NewsInfoModel
import com.teamwable.model.news.NewsMatchModel
import com.teamwable.model.news.NewsMatchScoreModel
import com.teamwable.model.news.NewsRankModel
import com.teamwable.network.dto.response.news.ResponseGameDto
import com.teamwable.network.dto.response.news.ResponseNewsInfoDto
import com.teamwable.network.dto.response.news.ResponseNoticeInfoDto
import com.teamwable.network.dto.response.news.ResponseRankDto
import com.teamwable.network.dto.response.news.ResponseScheduleDto

internal fun ResponseScheduleDto.toNewsMatchModel(): NewsMatchModel =
    NewsMatchModel(
        date = date,
        games = games.map { it.toNewsMatchScoreModel() },
    )

internal fun ResponseGameDto.toNewsMatchScoreModel(): NewsMatchScoreModel =
    NewsMatchScoreModel(
        gameDate = gameDate,
        aTeamName = aTeamName,
        aTeamScore = aTeamScore,
        bTeamName = bTeamName,
        bTeamScore = bTeamScore,
        gameStatus = gameStatus,
    )

internal fun ResponseRankDto.toNewsRankModel(): NewsRankModel =
    NewsRankModel(
        teamRank = teamRank,
        teamName = teamName,
        teamWin = teamWin,
        teamDefeat = teamDefeat,
        winningRate = winningRate,
        scoreDiff = scoreDiff,
    )

internal fun ResponseNewsInfoDto.toNewsInfoModel(): NewsInfoModel =
    NewsInfoModel(
        newsId = newsId,
        newsTitle = newsTitle,
        newsImage = newsImage,
        newsText = newsText,
        time = time,
    )

internal fun ResponseNoticeInfoDto.toNoticeInfoModel(): NewsInfoModel =
    NewsInfoModel(
        newsId = noticeId,
        newsTitle = noticeTitle,
        newsImage = noticeImage,
        newsText = noticeText,
        time = time
    )
