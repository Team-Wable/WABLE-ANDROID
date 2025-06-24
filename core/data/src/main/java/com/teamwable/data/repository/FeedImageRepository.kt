package com.teamwable.data.repository

interface FeedImageRepository {
    suspend fun saveToGallery(imageUrl: String): Result<Unit>
}
