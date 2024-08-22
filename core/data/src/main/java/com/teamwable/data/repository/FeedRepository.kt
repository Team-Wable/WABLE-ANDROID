package com.teamwable.data.repository

import androidx.paging.PagingData
import com.teamwable.model.Feed
import kotlinx.coroutines.flow.Flow

interface FeedRepository {
    fun getHomeFeeds(pageSize: Int): Flow<PagingData<Feed>>
}
