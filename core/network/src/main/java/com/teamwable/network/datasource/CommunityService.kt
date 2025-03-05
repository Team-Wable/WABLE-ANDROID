package com.teamwable.network.datasource

import com.teamwable.network.dto.response.community.ResponseCommunityInfoDto
import com.teamwable.network.dto.response.community.ResponseJoinedCommunityDto
import com.teamwable.network.util.BaseResponse
import com.teamwable.network.util.BaseUnitResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH

interface CommunityService {
    @GET("api/v1/community/list")
    suspend fun getCommunityList(): BaseResponse<List<ResponseCommunityInfoDto>>

    @GET("api/v1/community/member")
    suspend fun getJoinedCommunity(): BaseResponse<ResponseJoinedCommunityDto>

    @PATCH("api/v1/community/prein")
    suspend fun patchPreinCommunity(
        @Body communityName: String,
    ): BaseUnitResponse<Unit>
}
