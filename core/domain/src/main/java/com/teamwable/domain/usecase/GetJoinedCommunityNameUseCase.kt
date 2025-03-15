package com.teamwable.domain.usecase

import com.teamwable.data.repository.CommunityRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetJoinedCommunityNameUseCase @Inject constructor(
    private val communityRepository: CommunityRepository,
) {
    operator fun invoke(): Flow<String> =
        communityRepository.getJoinedCommunity()
}
