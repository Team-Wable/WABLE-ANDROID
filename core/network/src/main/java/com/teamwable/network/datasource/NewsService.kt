package com.teamwable.network.datasource

import com.teamwable.network.dto.response.main.ResponseNewsNumberDto
import com.teamwable.network.dto.response.news.ResponseGameTypeDto
import com.teamwable.network.dto.response.news.ResponseNewsInfoDto
import com.teamwable.network.dto.response.news.ResponseNoticeInfoDto
import com.teamwable.network.dto.response.news.ResponseRankDto
import com.teamwable.network.dto.response.news.ResponseScheduleDto
import com.teamwable.network.util.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {
    @GET("api/v1/information/gametype")
    suspend fun getGameType(): BaseResponse<ResponseGameTypeDto>

    @GET("api/v1/information/schedule")
    suspend fun getSchedule(): BaseResponse<List<ResponseScheduleDto>>

    @GET("api/v1/information/rank")
    suspend fun getRank(): BaseResponse<List<ResponseRankDto>>

    @GET("api/v1/information/news")
    suspend fun getNewsInfo(
        @Query(value = "cursor") contentId: Long = -1,
    ): BaseResponse<List<ResponseNewsInfoDto>>

    @GET("api/v1/information/notice")
    suspend fun getNoticeInfo(
        @Query(value = "cursor") contentId: Long = -1,
    ): BaseResponse<List<ResponseNoticeInfoDto>>

    @GET("api/v1/information/number")
    suspend fun getNumber(): BaseResponse<ResponseNewsNumberDto>
}
