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
import com.teamwable.network.util.handleThrowable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultCommentRepository @Inject constructor(
    private val apiService: CommentService,
) : CommentRepository {
    override fun getHomeDetailComments(feedId: Long): Flow<PagingData<Comment>> {
        val homeDetailCommentPagingSource = GenericPagingSource(
            apiCall = { cursor -> apiService.getHomeDetailComments(feedId, cursor).data },
            getNextCursor = { comments -> comments.lastOrNull()?.commentId },
        )

        return Pager(PagingConfig(pageSize = 15, prefetchDistance = 1)) {
            homeDetailCommentPagingSource
        }.flow.map { pagingData ->
            pagingData.map { it.toComment() }
        }
    }

    override fun getProfileComments(userId: Long): Flow<PagingData<Comment>> {
        val profileCommentPagingSource = GenericPagingSource(
            apiCall = { cursor -> apiService.getProfileComments(userId, cursor).data },
            getNextCursor = { comments -> comments.lastOrNull()?.commentId },
        )

        return Pager(PagingConfig(pageSize = 10, prefetchDistance = 1)) {
            profileCommentPagingSource
        }.flow.map { pagingData ->
            pagingData.map { it.toComment() }
        }
    }

    override suspend fun deleteComment(commentId: Long): Result<Boolean> = runCatching {
        apiService.deleteComment(commentId).success
    }.onFailure {
        return it.handleThrowable()
    }
}
