package com.teamwable.data.repository

import com.teamwable.model.auth.UserModel

interface AuthRepository {
    suspend fun postLogin(socialType: String, header: String): Result<UserModel>

    suspend fun postReissue(headerAccess: String, headerRefresh: String): Result<String>
}
