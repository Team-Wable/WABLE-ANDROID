package com.teamwable.data.repositoryimpl

import com.teamwable.data.mapper.toData.toReportDto
import com.teamwable.data.mapper.toModel.toProfile
import com.teamwable.data.repository.ProfileRepository
import com.teamwable.model.Profile
import com.teamwable.model.profile.MemberDataModel
import com.teamwable.network.datasource.ProfileService
import com.teamwable.network.dto.request.RequestWithdrawalDto
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

    override suspend fun getMemberData(): Result<MemberDataModel> {
        return runCatching {
            apiService.getMemberData().data.toMemberDataModel()
        }.onFailure { return it.handleThrowable() }
    }

    override suspend fun patchWithdrawal(deletedReason: List<String>): Result<Unit> {
        return runCatching {
            apiService.patchWithdrawal(RequestWithdrawalDto(deletedReason))
            Unit
        }.onFailure { return it.handleThrowable() }
    }

    override suspend fun postReport(nickname: String, relateText: String): Result<Unit> = runCatching {
        val request = Pair(nickname, relateText).toReportDto()
        apiService.postReport(request)
        Unit
    }.onFailure {
        return it.handleThrowable()
    }
}
