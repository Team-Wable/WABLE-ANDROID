package com.teamwable.data.repository

import androidx.paging.PagingData
import com.teamwable.model.news.NewsInfoModel
import com.teamwable.model.news.NewsMatchModel
import com.teamwable.model.news.NewsRankModel
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    suspend fun getGameType(): Result<String>

    suspend fun getSchedule(): Result<List<NewsMatchModel>>

    suspend fun getRank(): Result<List<NewsRankModel>>

    fun getNewsInfo(): Flow<PagingData<NewsInfoModel>>

    fun getNoticeInfo(): Flow<PagingData<NewsInfoModel>>

    suspend fun getNumber(): Result<Map<String, Int>>
}
