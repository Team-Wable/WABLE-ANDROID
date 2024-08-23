package com.teamwable.data.repository

import androidx.paging.PagingData
import com.teamwable.model.Comment
import kotlinx.coroutines.flow.Flow

interface CommentRepository {
    fun getHomeDetailComments(): Flow<PagingData<Comment>>
}
