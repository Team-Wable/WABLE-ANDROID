package com.teamwable.data.repository

import com.teamwable.model.Profile
import com.teamwable.model.profile.MemberDataModel
import com.teamwable.model.profile.MemberInfoEditModel

interface ProfileRepository {
    suspend fun getProfileInfo(userId: Long): Result<Profile>

    suspend fun getMemberData(): Result<MemberDataModel>

    suspend fun patchWithdrawal(deletedReason: List<String>): Result<Unit>

    suspend fun patchUserProfile(info: MemberInfoEditModel, imgUrl: String?): Result<Unit>

    suspend fun getNickNameDoubleCheck(nickname: String): Result<Unit>

    suspend fun postReport(nickname: String, relateText: String): Result<Unit>

    suspend fun postBan(banInfo: Triple<Long, String, Long>): Result<Unit>
}
