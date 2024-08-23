package com.teamwable.network.datasource

import com.teamwable.network.dto.request.RequestWithdrawalDto
import com.teamwable.network.dto.response.ResponseProfileInfoDto
import com.teamwable.network.dto.response.profile.ResponseMemberDataDto
import com.teamwable.network.util.BaseResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface ProfileService {
    @GET("api/v1/viewmember/{viewmemberId}")
    suspend fun getProfileInfo(
        @Path("viewmemberId") userId: Long,
    ): BaseResponse<ResponseProfileInfoDto>

    @GET("api/v1/member-data")
    suspend fun getMemberData(): BaseResponse<ResponseMemberDataDto>

    @PATCH("api/v1/withdrawal")
    suspend fun patchWithdrawal(@Body requestWithdrawalDto: RequestWithdrawalDto): BaseResponse<Unit>
}
