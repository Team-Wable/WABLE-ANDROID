package com.teamwable.data.repository

import androidx.paging.PagingData
import com.teamwable.model.Comment
import com.teamwable.model.Ghost
import kotlinx.coroutines.flow.Flow

interface CommentRepository {
    fun getHomeDetailComments(feedId: Long): Flow<PagingData<Comment>>

    fun getProfileComments(userId: Long): Flow<PagingData<Comment>>

    suspend fun deleteComment(commentId: Long): Result<Boolean>

    suspend fun postComment(contentId: Long, commentText: String): Result<Boolean>

    suspend fun postGhost(request: Ghost): Result<Boolean>
}
