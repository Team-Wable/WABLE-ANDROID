package com.teamwable.data.repositoryimpl

import com.teamwable.data.mapper.toModel.toNewsMatchModel
import com.teamwable.data.mapper.toModel.toNewsRankModel
import com.teamwable.data.repository.NewsRepository
import com.teamwable.model.news.NewsMatchModel
import com.teamwable.model.news.NewsRankModel
import com.teamwable.network.datasource.NewsService
import com.teamwable.network.util.handleThrowable
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
}
