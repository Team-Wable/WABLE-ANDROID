package com.teamwable.data.repositoryimpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.teamwable.data.mapper.toModel.toViewIt
import com.teamwable.data.repository.ViewItRepository
import com.teamwable.model.viewit.ViewIt
import com.teamwable.network.datasource.ViewItService
import com.teamwable.network.util.GenericPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultViewItRepository @Inject constructor(
    private val apiService: ViewItService,
) : ViewItRepository {
    override fun getViewIts(): Flow<PagingData<ViewIt>> {
        return Pager(PagingConfig(pageSize = 15, prefetchDistance = 1)) {
            GenericPagingSource(
                apiCall = { cursor -> apiService.getHomeFeeds(cursor).data },
                getNextCursor = { viewIts -> viewIts.lastOrNull()?.viewitId },
            )
        }.flow.map { pagingData ->
            pagingData.map { it.toViewIt() }
        }
    }
}
