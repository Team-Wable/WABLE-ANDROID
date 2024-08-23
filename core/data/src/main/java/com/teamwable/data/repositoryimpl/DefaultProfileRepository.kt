package com.teamwable.data.repositoryimpl

import com.teamwable.data.mapper.toModel.toMemberDataModel
import com.teamwable.data.repository.ProfileRepository
import com.teamwable.model.profile.MemberDataModel
import com.teamwable.network.datasource.ProfileService
import com.teamwable.network.util.handleThrowable
import javax.inject.Inject

internal class DefaultProfileRepository @Inject constructor(
    private val profileService: ProfileService,
) : ProfileRepository {
    override suspend fun getMemberData(): Result<MemberDataModel> {
        return runCatching {
            profileService.getMemberData().data.toMemberDataModel()
        }.onFailure { return it.handleThrowable() }
    }
}
