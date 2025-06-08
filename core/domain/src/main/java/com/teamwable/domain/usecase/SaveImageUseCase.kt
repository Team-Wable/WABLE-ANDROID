package com.teamwable.domain.usecase

import com.teamwable.data.repository.FeedImageRepository
import javax.inject.Inject

class SaveImageUseCase @Inject constructor(
    private val feedImageRepository: FeedImageRepository,
) {
    /**
         * Saves an image from the specified URL to the gallery.
         *
         * @param imageUrl The URL of the image to be saved.
         * @return A [Result] indicating success or failure of the save operation.
         */
        suspend operator fun invoke(imageUrl: String): Result<Unit> =
        feedImageRepository.saveToGallery(imageUrl)
}
