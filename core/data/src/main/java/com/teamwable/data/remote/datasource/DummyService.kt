package com.teamwable.data.remote.datasource

import com.teamwable.data.remote.dto.response.ResponseDummyDto
import com.teamwable.network.BaseResponse
import com.teamwable.network.BuildConfig.TEST_TOKEN
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

internal interface DummyService {
    @Headers("Authorization: Bearer $TEST_TOKEN")
    @GET("api/v2/contents")
    suspend fun getDummy(
        @Query(value = "cursor") contentId: Long = -1,
    ): BaseResponse<List<ResponseDummyDto>>
}
