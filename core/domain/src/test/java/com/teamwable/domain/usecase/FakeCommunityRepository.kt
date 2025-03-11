package com.teamwable.domain.usecase

import com.teamwable.data.repository.CommunityRepository
import com.teamwable.model.community.CommunityModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeCommunityRepository(
    private val communities: List<CommunityModel>,
    private val joinedCommunity: String,
) : CommunityRepository {
    override fun getCommunities(): Flow<List<CommunityModel>> = flowOf(communities)

    override fun getJoinedCommunity(): Flow<String> = flowOf(joinedCommunity)

    override suspend fun patchPreinCommunity(communityName: String): Result<Unit> {
        TODO("Not yet implemented")
    }
}
