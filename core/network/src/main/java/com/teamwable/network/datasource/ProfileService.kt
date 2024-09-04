package com.teamwable.network.datasource

import com.teamwable.network.dto.request.RequestWithdrawalDto
import com.teamwable.network.dto.response.ResponseProfileInfoDto
import com.teamwable.network.dto.response.profile.ResponseMemberDataDto
import com.teamwable.network.util.BaseResponse
import com.teamwable.network.util.BaseUnitResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Part
import retrofit2.http.Path

interface ProfileService {
    @GET("api/v1/viewmember/{viewmemberId}")
    suspend fun getProfileInfo(
        @Path("viewmemberId") userId: Long,
    ): BaseResponse<ResponseProfileInfoDto>

    @GET("api/v1/member-data")
    suspend fun getMemberData(): BaseResponse<ResponseMemberDataDto>

    @PATCH("api/v1/withdrawal")
    suspend fun patchWithdrawal(@Body requestWithdrawalDto: RequestWithdrawalDto): BaseUnitResponse<Unit>

    @PATCH("api/v1/user-profile2")
    suspend fun patchUserProfile(
        @Part("info") requestProfileEdit: RequestBody,
        @Part file: MultipartBody.Part?,
    ): BaseUnitResponse<Unit>
}
