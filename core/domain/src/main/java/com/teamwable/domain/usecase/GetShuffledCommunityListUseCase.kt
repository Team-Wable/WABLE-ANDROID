package com.teamwable.domain.usecase

import com.teamwable.data.repository.CommunityRepository
import com.teamwable.model.community.CommunityModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetShuffledCommunityListUseCase @Inject constructor(
    private val communityRepository: CommunityRepository,
) {
    operator fun invoke(): Flow<List<CommunityModel>> =
        communityRepository.getCommunities().map { it.shuffled() }
}
