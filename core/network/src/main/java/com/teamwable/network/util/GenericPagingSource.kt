package com.teamwable.network.util

import androidx.paging.PagingSource
import androidx.paging.PagingState

class GenericPagingSource<T : Any>(
    private val apiCall: suspend (Long) -> List<T>,
    private val getNextCursor: (List<T>) -> Long?,
) : PagingSource<Long, T>() {
    override fun getRefreshKey(state: PagingState<Long, T>): Long? {
        return null
    }

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, T> {
        return runCatching {
            val currentCursor = params.key ?: -1
            val items = apiCall(currentCursor)
            val nextCursor = getNextCursor(items)

            LoadResult.Page(
                data = items,
                prevKey = null,
                nextKey = nextCursor,
            )
        }.getOrElse {
            LoadResult.Error(it)
        }
    }
}
