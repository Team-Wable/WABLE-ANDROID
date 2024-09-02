package com.teamwable.network.datasource

import com.teamwable.network.dto.response.news.ResponseGameTypeDto
import com.teamwable.network.dto.response.news.ResponseRankDto
import com.teamwable.network.dto.response.news.ResponseScheduleDto
import com.teamwable.network.util.BaseResponse
import retrofit2.http.GET

interface NewsService {
    @GET("api/v1/information/gametype")
    suspend fun getGameType(): BaseResponse<ResponseGameTypeDto>

    @GET("api/v1/information/schedule")
    suspend fun getSchedule(): BaseResponse<List<ResponseScheduleDto>>

    @GET("api/v1/information/rank")
    suspend fun getRank(): BaseResponse<List<ResponseRankDto>>
}
