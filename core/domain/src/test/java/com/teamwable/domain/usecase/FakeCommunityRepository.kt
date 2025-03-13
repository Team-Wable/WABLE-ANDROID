package com.teamwable.domain.usecase

import com.teamwable.data.repository.CommunityRepository
import com.teamwable.model.community.CommunityModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal class FakeCommunityRepository(
    private val communities: List<CommunityModel>,
) : CommunityRepository {
    override fun getCommunities(): Flow<List<CommunityModel>> = flowOf(communities)

    override fun getJoinedCommunity(): Flow<String> = flowOf("")

    override suspend fun patchPreinCommunity(communityName: String): Result<Float> {
        return Result.success(0f)
    }
}
