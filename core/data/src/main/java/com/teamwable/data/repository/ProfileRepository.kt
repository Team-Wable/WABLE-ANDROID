package com.teamwable.data.repository

import com.teamwable.model.profile.MemberDataModel

interface ProfileRepository {
    suspend fun getMemberData(): Result<MemberDataModel>
}
