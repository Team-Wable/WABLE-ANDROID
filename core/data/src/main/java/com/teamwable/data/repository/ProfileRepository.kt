package com.teamwable.data.repository

import com.teamwable.model.Profile
import com.teamwable.model.profile.MemberDataModel
import com.teamwable.model.profile.MemberInfoEditModel
import java.io.File

interface ProfileRepository {
    suspend fun getProfileInfo(userId: Long): Result<Profile>
    suspend fun getMemberData(): Result<MemberDataModel>
    suspend fun patchWithdrawal(deletedReason: List<String>): Result<Unit>
    suspend fun patchProfileUriEdit(info: MemberInfoEditModel, file: File?): Result<Unit>
}
