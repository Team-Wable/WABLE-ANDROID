package com.teamwable.data.repositoryimpl

import com.teamwable.data.gallery.BitmapFetcher
import com.teamwable.data.gallery.GallerySaver
import com.teamwable.data.gallery.GallerySaver.Companion.FILE_EXTENSION
import com.teamwable.data.repository.FeedImageRepository
import com.teamwable.data.util.runHandledCatching
import javax.inject.Inject

internal class DefaultFeedImageRepository @Inject constructor(
    private val bitmapFetcher: BitmapFetcher,
    private val gallerySaver: GallerySaver,
) : FeedImageRepository {
    /**
     * Downloads an image from the given URL and saves it to the device gallery.
     *
     * @param imageUrl The URL of the image to download and save.
     * @return A [Result] indicating success or failure of the operation.
     */
    override suspend fun saveToGallery(imageUrl: String): Result<Unit> = runHandledCatching {
        val bitmap = bitmapFetcher.fetchBitmapFromUrl(imageUrl)
        gallerySaver.saveBitmapToGallery(bitmap, "wable_${System.currentTimeMillis()}.$FILE_EXTENSION")
    }
}
