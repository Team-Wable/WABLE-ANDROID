package com.teamwable.network.datasource

import com.teamwable.network.dto.response.profile.ResponseMemberDataDto
import com.teamwable.network.util.BaseResponse
import retrofit2.http.GET

interface ProfileService {
    @GET("api/v1/member-data")
    suspend fun getMemberData(): BaseResponse<ResponseMemberDataDto>
}
