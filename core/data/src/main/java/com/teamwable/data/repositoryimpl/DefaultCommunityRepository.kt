package com.teamwable.data.repositoryimpl

import com.teamwable.data.mapper.toModel.toCommunityModel
import com.teamwable.data.mapper.toModel.toRequestCommunityDto
import com.teamwable.data.repository.CommunityRepository
import com.teamwable.data.util.runSuspendCatching
import com.teamwable.model.community.CommunityModel
import com.teamwable.network.datasource.CommunityService
import com.teamwable.network.util.handleThrowable
import com.teamwable.network.util.toCustomError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class DefaultCommunityRepository @Inject constructor(
    private val communityService: CommunityService,
) : CommunityRepository {
    override fun getCommunities(): Flow<List<CommunityModel>> = flow {
        emit(communityService.getCommunityList().data.map { it.toCommunityModel() })
    }.catch {
        throw it.toCustomError()
    }

    override fun getJoinedCommunity(): Flow<String> = flow {
        emit(communityService.getJoinedCommunity().data.community.orEmpty())
    }.catch {
        throw it.toCustomError()
    }

    override suspend fun patchPreinCommunity(communityName: String): Result<Unit> = runSuspendCatching {
        communityService.patchPreinCommunity(communityName.toRequestCommunityDto())
        Unit
    }.onFailure {
        return it.handleThrowable()
    }
}
