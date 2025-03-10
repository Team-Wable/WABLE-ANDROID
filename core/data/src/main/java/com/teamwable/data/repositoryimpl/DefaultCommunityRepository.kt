package com.teamwable.data.repositoryimpl

import com.teamwable.data.mapper.toModel.toCommunityModel
import com.teamwable.data.mapper.toModel.toRequestCommunityDto
import com.teamwable.data.repository.CommunityRepository
import com.teamwable.data.util.runSuspendCatching
import com.teamwable.model.community.CommunityModel
import com.teamwable.network.datasource.CommunityService
import com.teamwable.network.util.handleThrowable
import javax.inject.Inject

internal class DefaultCommunityRepository @Inject constructor(
    private val communityService: CommunityService,
) : CommunityRepository {
    override suspend fun getCommunityInfo(): Result<List<CommunityModel>> = runSuspendCatching {
        communityService.getCommunityList().data.map { it.toCommunityModel() }
    }.onFailure {
        return it.handleThrowable()
    }

    override suspend fun getJoinedCommunity(): Result<String> = runSuspendCatching {
        communityService.getJoinedCommunity().data.community.orEmpty()
    }.onFailure {
        return it.handleThrowable()
    }

    override suspend fun patchPreinCommunity(communityName: String): Result<Unit> = runSuspendCatching {
        communityService.patchPreinCommunity(communityName.toRequestCommunityDto())
        Unit
    }.onFailure {
        return it.handleThrowable()
    }
}
