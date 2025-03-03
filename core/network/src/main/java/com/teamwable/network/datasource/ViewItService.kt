package com.teamwable.network.datasource

import com.teamwable.network.dto.response.viewit.ResponseViewItDto
import com.teamwable.network.util.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ViewItService {
    @GET("api/v1/viewit")
    suspend fun getHomeFeeds(
        @Query(value = "cursor") contentId: Long = -1,
    ): BaseResponse<List<ResponseViewItDto>>
}
