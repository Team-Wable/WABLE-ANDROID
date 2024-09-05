package com.teamwable.data.repositoryimpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.teamwable.data.mapper.toData.toPostCommentDto
import com.teamwable.data.mapper.toData.toPostCommentLikeDto
import com.teamwable.data.mapper.toData.toPostGhostDto
import com.teamwable.data.mapper.toModel.toComment
import com.teamwable.data.repository.CommentRepository
import com.teamwable.model.Comment
import com.teamwable.model.Ghost
import com.teamwable.network.datasource.CommentService
import com.teamwable.network.util.GenericPagingSource
import com.teamwable.network.util.handleThrowable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class DefaultCommentRepository @Inject constructor(
    private val apiService: CommentService,
) : CommentRepository {
    override fun getHomeDetailComments(feedId: Long): Flow<PagingData<Comment>> {
        return Pager(PagingConfig(pageSize = 15, prefetchDistance = 1)) {
            GenericPagingSource(
                apiCall = { cursor -> apiService.getHomeDetailComments(feedId, cursor).data },
                getNextCursor = { comments -> comments.lastOrNull()?.commentId },
            )
        }.flow.map { pagingData ->
            pagingData.map { it.toComment() }
        }
    }

    override fun getProfileComments(userId: Long): Flow<PagingData<Comment>> {
        return Pager(PagingConfig(pageSize = 10, prefetchDistance = 1)) {
            GenericPagingSource(
                apiCall = { cursor -> apiService.getProfileComments(userId, cursor).data },
                getNextCursor = { comments -> comments.lastOrNull()?.commentId },
            )
        }.flow.map { pagingData ->
            pagingData.map { it.toComment() }
        }
    }

    override suspend fun deleteComment(commentId: Long): Result<Unit> = runCatching {
        apiService.deleteComment(commentId)
        Unit
    }.onFailure {
        return it.handleThrowable()
    }

    override suspend fun postComment(contentId: Long, commentText: String): Result<Unit> = runCatching {
        val request = Pair(commentText, "comment").toPostCommentDto()
        apiService.postComment(contentId, request)
        Unit
    }.onFailure {
        return it.handleThrowable()
    }

    override suspend fun postGhost(request: Ghost): Result<Unit> = runCatching {
        apiService.postGhost(request.toPostGhostDto())
        Unit
    }.onFailure {
        return it.handleThrowable()
    }

    override suspend fun postCommentLike(commentId: Long, commentContent: String): Result<Unit> = runCatching {
        val request = Pair("commentLiked", commentContent).toPostCommentLikeDto()
        apiService.postCommentLike(commentId, request)
        Unit
    }.onFailure {
        return it.handleThrowable()
    }

    override suspend fun deleteCommentLike(commentId: Long): Result<Unit> = runCatching {
        apiService.deleteCommentLike(commentId)
        Unit
    }.onFailure {
        return it.handleThrowable()
    }
}
