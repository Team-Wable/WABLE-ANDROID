package com.teamwable.data.repository

import com.teamwable.model.community.CommunityModel

interface CommunityRepository {
    suspend fun getCommunityInfo(): Result<List<CommunityModel>>

    suspend fun getJoinedCommunity(): Result<String>

    suspend fun patchPreinCommunity(communityName: String): Result<Unit>
}
