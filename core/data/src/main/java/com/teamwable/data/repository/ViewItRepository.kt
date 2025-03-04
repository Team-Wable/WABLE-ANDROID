package com.teamwable.data.repository

import androidx.paging.PagingData
import com.teamwable.model.viewit.LinkInfo
import com.teamwable.model.viewit.ViewIt
import kotlinx.coroutines.flow.Flow

interface ViewItRepository {
    fun getViewIts(): Flow<PagingData<ViewIt>>

    suspend fun postViewIt(link: String, viewItContent: String): Result<LinkInfo>
}
