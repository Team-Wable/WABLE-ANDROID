package com.teamwable.data.repositoryimpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.teamwable.data.mapper.toModel.toNewsInfoModel
import com.teamwable.data.mapper.toModel.toNewsMatchModel
import com.teamwable.data.mapper.toModel.toNewsRankModel
import com.teamwable.data.mapper.toModel.toNoticeInfoModel
import com.teamwable.data.repository.NewsRepository
import com.teamwable.model.news.NewsInfoModel
import com.teamwable.model.news.NewsMatchModel
import com.teamwable.model.news.NewsRankModel
import com.teamwable.network.datasource.NewsService
import com.teamwable.network.util.GenericPagingSource
import com.teamwable.network.util.handleThrowable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class DefaultNewsRepository @Inject constructor(
    private val newsService: NewsService,
) : NewsRepository {
    override suspend fun getGameType(): Result<String> {
        return runCatching {
            newsService.getGameType().data.lckGameType
        }.onFailure { return it.handleThrowable() }
    }

    override suspend fun getSchedule(): Result<List<NewsMatchModel>> {
        return runCatching {
            newsService.getSchedule().data.map { it.toNewsMatchModel() }
        }.onFailure { return it.handleThrowable() }
    }

    override suspend fun getRank(): Result<List<NewsRankModel>> {
        return runCatching {
            newsService.getRank().data.map { it.toNewsRankModel() }
        }.onFailure { return it.handleThrowable() }
    }

    override fun getNewsInfo(): Flow<PagingData<NewsInfoModel>> {
        return Pager(PagingConfig(pageSize = 15, prefetchDistance = 1)) {
            GenericPagingSource(
                apiCall = { cursor -> newsService.getNewsInfo(cursor).data },
                getNextCursor = { feeds -> feeds.lastOrNull()?.newsId },
            )
        }.flow.map { pagingData ->
            pagingData.map { it.toNewsInfoModel() }
        }
    }

    override fun getNoticeInfo(): Flow<PagingData<NewsInfoModel>> {
        return Pager(PagingConfig(pageSize = 15, prefetchDistance = 1)) {
            GenericPagingSource(
                apiCall = { cursor -> newsService.getNoticeInfo(cursor).data },
                getNextCursor = { feeds -> feeds.lastOrNull()?.noticeId },
            )
        }.flow.map { pagingData ->
            pagingData.map { it.toNoticeInfoModel() }
        }
    }

    override suspend fun getNumber(): Result<Map<String, Int>> {
        return runCatching {
            mapOf(
                "news" to newsService.getNumber().data.newsNumber,
                "notice" to newsService.getNumber().data.noticeNumber
            )
        }.onFailure { return it.handleThrowable() }
    }
}
