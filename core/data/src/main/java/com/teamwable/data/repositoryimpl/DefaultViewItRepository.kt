package com.teamwable.data.repositoryimpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.teamwable.data.mapper.toData.toPostViewItDto
import com.teamwable.data.mapper.toModel.toViewIt
import com.teamwable.data.repository.ViewItRepository
import com.teamwable.data.util.JsoupParser
import com.teamwable.model.viewit.LinkInfo
import com.teamwable.model.viewit.ViewIt
import com.teamwable.network.datasource.ViewItService
import com.teamwable.network.util.GenericPagingSource
import com.teamwable.network.util.handleThrowable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
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

    override suspend fun postViewIt(link: String, viewItContent: String): Result<LinkInfo> {
        return runCatching {
            withContext(Dispatchers.IO) {
                JsoupParser.parseMetadata(link).copy(viewItText = viewItContent)
            }
        }.recoverCatching {
            handleJsoupFailure(link, viewItContent)
        }.onSuccess { linkInfo ->
            apiService.postViewIt(linkInfo.toPostViewItDto())
        }.onFailure {
            return it.handleThrowable()
        }
    }

    private fun handleJsoupFailure(link: String, viewItContent: String) = LinkInfo(
        linkImage = DEFAULT_VIEW_IT,
        link = link,
        linkTitle = INVALID_LINK_TITLE,
        viewItText = viewItContent,
        linkName = link,
    )

    override suspend fun postViewItLike(viewItId: Long): Result<Unit> = runCatching {
        apiService.postViewItLike(viewItId)
        Unit
    }.onFailure {
        return it.handleThrowable()
    }

    override suspend fun deleteViewItLike(viewItId: Long): Result<Unit> = runCatching {
        apiService.deleteViewItLike(viewItId)
        Unit
    }.onFailure {
        return it.handleThrowable()
    }

    override suspend fun deleteViewIt(viewItId: Long): Result<Unit> = runCatching {
        apiService.deleteViewIt(viewItId)
        Unit
    }.onFailure {
        return it.handleThrowable()
    }

    companion object {
        const val DEFAULT_VIEW_IT = "DEFAULT"
        private const val ERROR_INVALID_LINK = "잘못된 링크입니다"
        private const val INVALID_LINK_TITLE = "불러올 수 없는 링크입니다."
    }
}
