package com.teamwable.data.repository

import com.teamwable.model.Profile

interface ProfileRepository {
    suspend fun getProfileInfo(userId: Long): Result<Profile>

    suspend fun postReport(nickname: String, relateText: String): Result<Unit>
}
