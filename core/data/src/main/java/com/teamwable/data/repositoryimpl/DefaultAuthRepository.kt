package com.teamwable.data.repositoryimpl

import com.teamwable.data.mapper.toModel.toUserModel
import com.teamwable.data.repository.AuthRepository
import com.teamwable.data.util.runHandledCatching
import com.teamwable.model.auth.UserModel
import com.teamwable.network.datasource.AuthService
import com.teamwable.network.dto.request.RequestSocialLoginDto
import javax.inject.Inject

internal class DefaultAuthRepository @Inject constructor(
    private val authService: AuthService,
) : AuthRepository {
    /**
     * Attempts to log in a user using the specified social authentication type and header.
     *
     * @param socialType The type of social authentication provider (e.g., "google", "facebook").
     * @param header The authentication header required by the social provider.
     * @return A [Result] containing the authenticated [UserModel] on success, or an error on failure.
     */
    override suspend fun postLogin(socialType: String, header: String): Result<UserModel> = runHandledCatching {
        authService.postLogin(RequestSocialLoginDto(socialType), header).data.toUserModel()
    }
}
