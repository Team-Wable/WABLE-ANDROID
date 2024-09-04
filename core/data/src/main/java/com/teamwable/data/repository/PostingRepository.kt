package com.teamwable.data.repository

interface PostingRepository {
    suspend fun postingMultiPart(title: String, content: String, uriImage: String?): Result<Unit>
}
