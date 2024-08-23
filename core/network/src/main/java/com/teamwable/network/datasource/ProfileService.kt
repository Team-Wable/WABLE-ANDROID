package com.teamwable.network.datasource

import com.teamwable.network.dto.response.ResponseProfileInfoDto
import com.teamwable.network.util.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ProfileService {
    @GET("api/v1/viewmember/{viewmemberId}")
    suspend fun getProfileInfo(
        @Path("viewmemberId") userId: Long,
    ): BaseResponse<ResponseProfileInfoDto>
}
