package com.teamwable.data.repositoryimpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.teamwable.data.mapper.toData.toPostGhostDto
import com.teamwable.data.mapper.toModel.toFeed
import com.teamwable.data.repository.FeedRepository
import com.teamwable.model.Feed
import com.teamwable.model.Ghost
import com.teamwable.network.datasource.FeedService
import com.teamwable.network.util.GenericPagingSource
import com.teamwable.network.util.handleThrowable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultFeedRepository @Inject constructor(
    private val apiService: FeedService,
) : FeedRepository {
    override fun getHomeFeeds(): Flow<PagingData<Feed>> {
        return Pager(PagingConfig(pageSize = 20, prefetchDistance = 1)) {
            GenericPagingSource(
                apiCall = { cursor -> apiService.getHomeFeeds(cursor).data },
                getNextCursor = { feeds -> feeds.lastOrNull()?.contentId },
            )
        }.flow.map { pagingData ->
            pagingData.map { it.toFeed() }
        }
    }

    override fun getProfileFeeds(userId: Long): Flow<PagingData<Feed>> {
        return Pager(PagingConfig(pageSize = 15, prefetchDistance = 1)) {
            GenericPagingSource(
                apiCall = { cursor -> apiService.getProfileFeeds(userId, cursor).data },
                getNextCursor = { feeds -> feeds.lastOrNull()?.contentId },
            )
        }.flow.map { pagingData ->
            pagingData.map { it.toFeed() }
        }
    }

    override suspend fun deleteFeed(feedId: Long): Result<Boolean> = runCatching {
        apiService.deleteFeed(feedId).success
    }.onFailure {
        return it.handleThrowable()
    }

    override suspend fun getHomeDetail(feedId: Long): Result<Feed> = runCatching {
        apiService.getHomeDetail(feedId).data.toFeed()
    }.onFailure {
        return it.handleThrowable()
    }

    override suspend fun postGhost(request: Ghost): Result<Boolean> = runCatching {
        apiService.postGhost(request.toPostGhostDto()).success
    }.onFailure {
        return it.handleThrowable()
    }
}
