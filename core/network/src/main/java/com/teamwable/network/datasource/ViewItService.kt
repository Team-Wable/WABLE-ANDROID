package com.teamwable.network.datasource

import com.teamwable.network.dto.request.viewit.RequestPostViewItDto
import com.teamwable.network.dto.response.viewit.ResponseViewItDto
import com.teamwable.network.util.BaseResponse
import com.teamwable.network.util.BaseUnitResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ViewItService {
    @GET("api/v1/viewit")
    suspend fun getHomeFeeds(
        @Query(value = "cursor") contentId: Long = -1,
    ): BaseResponse<List<ResponseViewItDto>>

    @POST("api/v1/viewit")
    suspend fun postViewIt(
        @Body linkInfo: RequestPostViewItDto,
    ): BaseUnitResponse<Unit>

    @POST("api/v1/viewit/{viewitId}/liked")
    suspend fun postViewItLike(
        @Path(value = "viewitId") viewitId: Long,
    ): BaseUnitResponse<Unit>

    @DELETE("api/v1/viewit/{viewitId}/unliked")
    suspend fun deleteViewItLike(
        @Path(value = "viewitId") viewitId: Long,
    ): BaseUnitResponse<Unit>

    @DELETE("api/v1/viewit/{viewitId}")
    suspend fun deleteViewIt(
        @Path(value = "viewitId") viewitId: Long,
    ): BaseUnitResponse<Unit>
}
