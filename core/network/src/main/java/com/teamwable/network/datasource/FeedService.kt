package com.teamwable.network.datasource

import com.teamwable.network.dto.request.RequestGhostDto
import com.teamwable.network.dto.request.RequestPostLikeDto
import com.teamwable.network.dto.response.ResponseFeedDto
import com.teamwable.network.util.BaseResponse
import com.teamwable.network.util.BaseUnitResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface FeedService {
    @GET("api/v2/contents")
    suspend fun getHomeFeeds(
        @Query(value = "cursor") contentId: Long = -1,
    ): BaseResponse<List<ResponseFeedDto>>

    @GET("api/v2/member/{memberId}/contents")
    suspend fun getProfileFeeds(
        @Path("memberId") userId: Long,
        @Query(value = "cursor") contentId: Long = -1,
    ): BaseResponse<List<ResponseFeedDto>>

    @DELETE("api/v1/content/{contentId}")
    suspend fun deleteFeed(
        @Path(value = "contentId") contentId: Long,
    ): BaseUnitResponse<Unit>

    @GET("api/v2/content/{contentId}")
    suspend fun getHomeDetail(
        @Path(value = "contentId") contentId: Long,
    ): BaseResponse<ResponseFeedDto>

    @POST("api/v1/ghost2")
    suspend fun postGhost(
        @Body request: RequestGhostDto,
    ): BaseUnitResponse<Unit>

    @POST("api/v1/content/{contentId}/liked")
    suspend fun postFeedLike(
        @Path(value = "contentId") contentId: Long,
        @Body alarmTriggerType: RequestPostLikeDto,
    ): BaseUnitResponse<Unit>

    @DELETE("api/v1/content/{contentId}/unliked")
    suspend fun deleteFeedLike(
        @Path(value = "contentId") contentId: Long,
    ): BaseUnitResponse<Unit>
}
