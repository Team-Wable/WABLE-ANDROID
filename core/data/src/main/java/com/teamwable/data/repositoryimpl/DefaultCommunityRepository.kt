package com.teamwable.data.repositoryimpl

import com.teamwable.data.mapper.toModel.toCommunityModel
import com.teamwable.data.mapper.toModel.toRequestCommunityDto
import com.teamwable.data.repository.CommunityRepository
import com.teamwable.data.util.runHandledCatching
import com.teamwable.model.community.CommunityModel
import com.teamwable.network.datasource.CommunityService
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

    /**
     * Returns a flow emitting the name of the joined community.
     *
     * Emits an empty string if no community is joined. Errors are rethrown as custom errors.
     *
     * @return A [Flow] emitting the joined community name as a [String].
     */
    override fun getJoinedCommunity(): Flow<String> = flow {
        emit(communityService.getJoinedCommunity().data.community.orEmpty())
    }.catch {
        throw it.toCustomError()
    }

    /**
     * Updates the prein community with the specified name and returns the associated community number.
     *
     * @param communityName The name of the community to update.
     * @return A [Result] containing the community number as a [Float] if successful, or an error if the operation fails.
     */
    override suspend fun patchPreinCommunity(communityName: String): Result<Float> = runHandledCatching {
        communityService.patchPreinCommunity(communityName.toRequestCommunityDto()).data
            .toCommunityModel().communityNum
    }
}
