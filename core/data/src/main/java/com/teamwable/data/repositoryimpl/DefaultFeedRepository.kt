package com.teamwable.data.repositoryimpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.teamwable.data.mapper.toModel.toFeed
import com.teamwable.data.repository.FeedRepository
import com.teamwable.model.Feed
import com.teamwable.network.datasource.FeedPagingSource
import com.teamwable.network.datasource.FeedService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultFeedRepository @Inject constructor(
    private val apiService: FeedService,
) : FeedRepository {
    override fun getFeeds(pageSize: Int): Flow<PagingData<Feed>> {
        return Pager(PagingConfig(pageSize, prefetchDistance = 0)) {
            FeedPagingSource(apiService)
        }.flow.map { pagingData ->
            pagingData.map { it.toFeed() }
        }
    }
}
