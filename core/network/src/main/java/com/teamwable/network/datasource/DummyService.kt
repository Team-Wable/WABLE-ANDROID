package com.teamwable.network.datasource

import com.teamwable.network.dto.response.ResponseDummyDto
import com.teamwable.network.util.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface DummyService {
    @Headers("Authorization: Bearer ")
    @GET("api/v2/contents")
    suspend fun getDummy(
        @Query(value = "cursor") contentId: Long = -1,
    ): BaseResponse<List<ResponseDummyDto>>
}
