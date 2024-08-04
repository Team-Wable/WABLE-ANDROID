package com.teamwable.data.remote.datasource

import com.teamwable.data.BuildConfig.TEST_TOKEN
import com.teamwable.data.remote.dto.response.ResponseDummyDto
import com.teamwable.data.util.EndPoints.API
import com.teamwable.data.util.EndPoints.CONTENTS
import com.teamwable.data.util.EndPoints.CURSOR
import com.teamwable.data.util.EndPoints.V2
import com.teamwable.network.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface DummyService {
    @Headers("Authorization: Bearer $TEST_TOKEN")
    @GET("$API/$V2/$CONTENTS")
    suspend fun getDummy(
        @Query(value = CURSOR) contentId: Long = -1,
    ): BaseResponse<List<ResponseDummyDto>>
}
