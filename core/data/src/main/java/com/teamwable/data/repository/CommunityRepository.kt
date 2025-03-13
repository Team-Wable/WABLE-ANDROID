package com.teamwable.data.repository

import com.teamwable.model.community.CommunityModel
import kotlinx.coroutines.flow.Flow

interface CommunityRepository {
    fun getCommunities(): Flow<List<CommunityModel>>

    fun getJoinedCommunity(): Flow<String>

    suspend fun patchPreinCommunity(communityName: String): Result<Float>
}
