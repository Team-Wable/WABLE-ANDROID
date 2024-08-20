package com.teamwable.data.repository

import com.teamwable.model.auth.UserModel

interface AuthRepository {
    suspend fun postLogin(socialType: String, header: String): Result<UserModel>

}
