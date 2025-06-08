package com.teamwable.data.repository

interface FeedImageRepository {
    /**
 * Saves an image specified by its URL to the gallery asynchronously.
 *
 * @param imageUrl The URL of the image to be saved.
 * @return A [Result] indicating success or failure of the save operation.
 */
suspend fun saveToGallery(imageUrl: String): Result<Unit>
}
