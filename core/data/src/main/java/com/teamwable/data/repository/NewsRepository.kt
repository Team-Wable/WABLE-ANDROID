package com.teamwable.data.repository

import com.teamwable.model.news.NewsMatchModel
import com.teamwable.model.news.NewsRankModel

interface NewsRepository {
    suspend fun getGameType(): Result<String>
    suspend fun getSchedule(): Result<List<NewsMatchModel>>
    suspend fun getRank(): Result<List<NewsRankModel>>
}
