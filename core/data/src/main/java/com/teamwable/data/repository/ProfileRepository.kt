package com.teamwable.data.repository

import com.teamwable.model.Profile
import com.teamwable.model.profile.MemberDataModel

interface ProfileRepository {
    suspend fun getProfileInfo(userId: Long): Result<Profile>
    suspend fun getMemberData(): Result<MemberDataModel>
    suspend fun patchWithdrawal(deletedReason: List<String>): Result<Unit>
}
