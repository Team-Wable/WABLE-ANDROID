package com.teamwable.data.repositoryimpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.teamwable.data.mapper.toModel.toViewIt
import com.teamwable.data.repository.ViewItRepository
import com.teamwable.model.network.Error
import com.teamwable.model.viewit.ViewIt
import com.teamwable.network.datasource.ViewItService
import com.teamwable.network.util.GenericPagingSource
import com.teamwable.network.util.formatUrl
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

    override suspend fun getLinkInfo(link: String): Result<Unit> = runCatching {
        val document = withContext(Dispatchers.IO) {
            val formattedUrl = formatUrl(link)
            Jsoup.connect(formattedUrl).get()
        }

        val title = document.select("meta[property=og:title]").attr("content").ifEmpty {
            document.title()
        }
        val imageUrl = document.select("meta[property=og:image]").attr("content")
        val siteName = document.select("meta[property=og:site_name]").attr("content").ifEmpty {
            URL(link).host
        }
    }.recoverCatching {
        return Result.failure(Error.UnknownError("잘못된 링크입니다"))
    }
}
