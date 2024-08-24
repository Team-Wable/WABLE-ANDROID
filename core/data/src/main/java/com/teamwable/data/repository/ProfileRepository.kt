package com.teamwable.data.repository

import com.teamwable.model.Profile

interface ProfileRepository {
    suspend fun getProfileInfo(userId: Long): Result<Profile>
}
