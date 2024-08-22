package com.teamwable.network.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.teamwable.network.dto.response.ResponseFeedDto

class FeedPagingSource(
    private val apiService: FeedService,
) : PagingSource<Long, ResponseFeedDto>() {
    override fun getRefreshKey(state: PagingState<Long, ResponseFeedDto>): Long? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestItemToPosition(anchorPosition)?.contentId
        }
    }

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, ResponseFeedDto> {
        return runCatching {
            val currentCursor = params.key ?: -1
            val feeds = apiService.getFeeds(contentId = currentCursor).data
            val nextCursor = if (feeds.isNotEmpty()) feeds.last().contentId else null

            LoadResult.Page(
                data = feeds,
                prevKey = null,
                nextKey = nextCursor,
            )
        }.getOrElse {
            LoadResult.Error(it)
        }
    }
}
