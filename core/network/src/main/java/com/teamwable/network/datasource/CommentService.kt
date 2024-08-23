package com.teamwable.network.datasource

import com.teamwable.network.dto.response.ResponseCommentDto
import com.teamwable.network.util.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CommentService {
    @GET("api/v2/content/{contentId}/comments")
    suspend fun getHomeDetailComments(
        @Path(value = "contentId") contentId: Long,
        @Query(value = "cursor") cursor: Long = -1,
    ): BaseResponse<List<ResponseCommentDto>>
}
