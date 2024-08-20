package com.teamwable.data.repositoryimpl

import com.teamwable.data.mapper.toModel.toUserModel
import com.teamwable.data.repository.AuthRepository
import com.teamwable.model.auth.UserModel
import com.teamwable.network.datasource.AuthService
import com.teamwable.network.dto.request.RequestSocialLoginDto
import com.teamwable.network.util.handleThrowable
import javax.inject.Inject

internal class DefaultAuthRepository @Inject constructor(
    private val authService: AuthService,
) : AuthRepository {
    override suspend fun postLogin(socialType: String, header: String): Result<UserModel> = runCatching {
        authService.postLogin(RequestSocialLoginDto(socialType), header).data.toUserModel()
    }.onFailure {
        return it.handleThrowable()
    }


}
