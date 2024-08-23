package com.teamwable.data.repositoryimpl

import com.teamwable.data.mapper.toModel.toProfile
import com.teamwable.data.repository.ProfileRepository
import com.teamwable.model.Profile
import com.teamwable.network.datasource.ProfileService
import com.teamwable.network.util.handleThrowable
import javax.inject.Inject

class DefaultProfileRepository @Inject constructor(
    private val apiService: ProfileService,
) : ProfileRepository {
    override suspend fun getProfileInfo(userId: Long): Result<Profile> = runCatching {
        apiService.getProfileInfo(userId).data.toProfile()
    }.onFailure {
        return it.handleThrowable()
    }
}
