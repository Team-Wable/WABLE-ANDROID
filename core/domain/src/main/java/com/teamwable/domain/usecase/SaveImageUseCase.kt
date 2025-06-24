package com.teamwable.domain.usecase

import com.teamwable.data.repository.FeedImageRepository
import javax.inject.Inject

class SaveImageUseCase @Inject constructor(
    private val feedImageRepository: FeedImageRepository,
) {
    suspend operator fun invoke(imageUrl: String): Result<Unit> =
        feedImageRepository.saveToGallery(imageUrl)
}
