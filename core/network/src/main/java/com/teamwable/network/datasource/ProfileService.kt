package com.teamwable.network.datasource

import com.teamwable.network.dto.request.RequestReportDto
import com.teamwable.network.dto.response.ResponseProfileInfoDto
import com.teamwable.network.util.BaseResponse
import com.teamwable.network.util.BaseUnitResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ProfileService {
    @GET("api/v1/viewmember/{viewmemberId}")
    suspend fun getProfileInfo(
        @Path("viewmemberId") userId: Long,
    ): BaseResponse<ResponseProfileInfoDto>

    @POST("api/v1/report/slack")
    suspend fun postReport(
        @Body request: RequestReportDto,
    ): BaseUnitResponse<Unit>
}
