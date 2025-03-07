package com.teamwable.data.repositoryimpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.teamwable.data.mapper.toData.toPostViewItDto
import com.teamwable.data.mapper.toModel.toViewIt
import com.teamwable.data.repository.ViewItRepository
import com.teamwable.model.network.Error
import com.teamwable.model.viewit.LinkInfo
import com.teamwable.model.viewit.ViewIt
import com.teamwable.network.datasource.ViewItService
import com.teamwable.network.util.GenericPagingSource
import com.teamwable.network.util.formatUrl
import com.teamwable.network.util.handleThrowable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.net.URL
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

    override suspend fun postViewIt(link: String, viewItContent: String): Result<LinkInfo> = runCatching {
        val document = withContext(Dispatchers.IO) {
            val formattedUrl = formatUrl(link)
            Jsoup.connect(formattedUrl).get()
        }
        val imageUrl = document.select(META_OG_IMAGE).attr("content").ifEmpty { DEFAULT_VIEW_IT }
        val title = document.select(META_OG_TITLE).attr("content").ifEmpty { document.title() }
        val linkName = document.select(META_OG_SITE_NAME).attr("content").ifEmpty { URL(link).host }

        LinkInfo(imageUrl, link, title, viewItContent, linkName)
    }.recoverCatching {
        return Result.failure(Error.CustomError(ERROR_INVALID_LINK))
    }.onSuccess {
        apiService.postViewIt(it.toPostViewItDto())
    }.onFailure {
        return it.handleThrowable()
    }

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
        private const val META_OG_IMAGE = "meta[property=og:image]"
        private const val META_OG_TITLE = "meta[property=og:title]"
        private const val META_OG_SITE_NAME = "meta[property=og:site_name]"
        private const val ERROR_INVALID_LINK = "잘못된 링크입니다"
    }
}
