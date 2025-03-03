package com.teamwable.data.repository

import androidx.paging.PagingData
import com.teamwable.model.home.Comment
import com.teamwable.model.home.Ghost
import kotlinx.coroutines.flow.Flow

interface CommentRepository {
    fun getHomeDetailComments(feedId: Long): Flow<PagingData<Comment>>

    fun getProfileComments(userId: Long): Flow<PagingData<Comment>>

    suspend fun deleteComment(commentId: Long): Result<Unit>

    suspend fun postComment(contentId: Long, commentInfo: Triple<String, Long, Long>): Result<Unit>

    suspend fun postGhost(request: Ghost): Result<Unit>

    suspend fun postCommentLike(commentId: Long, commentContent: String): Result<Unit>

    suspend fun deleteCommentLike(commentId: Long): Result<Unit>
}
