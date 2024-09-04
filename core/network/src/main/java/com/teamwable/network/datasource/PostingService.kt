package com.teamwable.network.datasource

import com.teamwable.network.util.BaseUnitResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface PostingService {
    @Multipart
    @POST("/api/v2/content")
    suspend fun postingMultiPart(
        @Part("text") text: RequestBody,
        @Part image: MultipartBody.Part?,
    ): BaseUnitResponse<Unit>
}
