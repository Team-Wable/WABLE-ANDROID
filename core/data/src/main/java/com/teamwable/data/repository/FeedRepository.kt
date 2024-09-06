package com.teamwable.data.repository

import androidx.paging.PagingData
import com.teamwable.model.Feed
import com.teamwable.model.Ghost
import kotlinx.coroutines.flow.Flow

interface FeedRepository {
    fun getHomeFeeds(): Flow<PagingData<Feed>>

    fun getProfileFeeds(userId: Long): Flow<PagingData<Feed>>

    suspend fun deleteFeed(feedId: Long): Result<Boolean>

    suspend fun getHomeDetail(feedId: Long): Result<Feed>

    suspend fun postGhost(request: Ghost): Result<Boolean>

    suspend fun postFeedLike(feedId: Long): Result<Unit>

    suspend fun deleteFeedLike(feedId: Long): Result<Unit>
}
