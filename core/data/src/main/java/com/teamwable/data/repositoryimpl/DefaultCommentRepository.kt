package com.teamwable.data.repositoryimpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.teamwable.data.mapper.toModel.toComment
import com.teamwable.data.repository.CommentRepository
import com.teamwable.model.Comment
import com.teamwable.network.datasource.CommentService
import com.teamwable.network.util.GenericPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultCommentRepository @Inject constructor(
    private val apiService: CommentService,
) : CommentRepository {
    override fun getHomeDetailComments(feedId: Long): Flow<PagingData<Comment>> {
        val homeFeedPagingSource = GenericPagingSource(
            apiCall = { cursor -> apiService.getHomeDetailComments(feedId, cursor).data },
            getNextCursor = { comments -> comments.lastOrNull()?.commentId },
        )

        return Pager(PagingConfig(pageSize = 15, prefetchDistance = 0)) {
            homeFeedPagingSource
        }.flow.map { pagingData ->
            pagingData.map { it.toComment() }
        }
    }
}
