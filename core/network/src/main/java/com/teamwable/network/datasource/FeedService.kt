package com.teamwable.network.datasource

import com.teamwable.network.dto.response.ResponseFeedDto
import com.teamwable.network.util.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface FeedService {
    @GET("api/v2/contents")
    suspend fun getFeeds(
        @Query(value = "cursor") contentId: Long = -1,
    ): BaseResponse<List<ResponseFeedDto>>
}
