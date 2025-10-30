package com.teamwable.network.datasource

import com.teamwable.network.dto.request.RequestCommunityDto
import com.teamwable.network.dto.response.community.ResponseCommunityInfoDto
import com.teamwable.network.dto.response.community.ResponseJoinedCommunityDto
import com.teamwable.network.util.BaseResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH

interface CommunityService {
    @GET("api/v1/community/list")
    suspend fun getCommunityList(): BaseResponse<List<ResponseCommunityInfoDto>>

    @GET("api/v1/community/member")
    suspend fun getJoinedCommunity(): BaseResponse<ResponseJoinedCommunityDto>

    @PATCH("api/v2/community/prein")
    suspend fun patchPreinCommunity(
        @Body requestCommunityDto: RequestCommunityDto,
    ): BaseResponse<ResponseCommunityInfoDto>
}
