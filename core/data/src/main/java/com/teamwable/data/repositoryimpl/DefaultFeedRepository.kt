package com.teamwable.data.repositoryimpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.teamwable.data.mapper.toModel.toFeed
import com.teamwable.data.repository.FeedRepository
import com.teamwable.model.Feed
import com.teamwable.network.datasource.FeedService
import com.teamwable.network.util.GenericPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultFeedRepository @Inject constructor(
    private val apiService: FeedService,
) : FeedRepository {
    override fun getHomeFeeds(): Flow<PagingData<Feed>> {
        val homeFeedPagingSource = GenericPagingSource(
            apiCall = { cursor -> apiService.getHomeFeeds(cursor).data },
            getNextCursor = { feeds -> feeds.lastOrNull()?.contentId },
        )

        return Pager(PagingConfig(pageSize = 20, prefetchDistance = 0)) {
            homeFeedPagingSource
        }.flow.map { pagingData ->
            pagingData.map { it.toFeed() }
        }
    }

    override fun getProfileFeeds(userId: Long): Flow<PagingData<Feed>> {
        val profileFeedPagingSource = GenericPagingSource(
            apiCall = { cursor -> apiService.getProfileFeeds(userId, cursor).data },
            getNextCursor = { feeds -> feeds.lastOrNull()?.contentId },
        )

        return Pager(PagingConfig(pageSize = 15, prefetchDistance = 0)) {
            profileFeedPagingSource
        }.flow.map { pagingData ->
            pagingData.map { it.toFeed() }
        }
    }
}
