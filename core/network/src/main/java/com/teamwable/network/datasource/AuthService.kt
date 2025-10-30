package com.teamwable.network.datasource

import com.teamwable.network.dto.request.RequestSocialLoginDto
import com.teamwable.network.dto.response.auth.ResponseReissueTokenDto
import com.teamwable.network.dto.response.auth.ResponseSocialLoginDto
import com.teamwable.network.util.BaseResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {
    @POST("api/v2/auth")
    suspend fun postLogin(
        @Body requestSocialLogin: RequestSocialLoginDto,
        @Header("Authorization") kakaoToken: String,
    ): BaseResponse<ResponseSocialLoginDto>

    @GET("api/v1/auth/token")
    suspend fun getReissueToken(
        @Header("Authorization") accessToken: String,
        @Header("Refresh") refreshToken: String,
    ): BaseResponse<ResponseReissueTokenDto>
}
