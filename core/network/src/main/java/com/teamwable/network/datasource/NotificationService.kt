package com.teamwable.network.datasource

import com.teamwable.network.dto.response.notification.ResponseInformationDto
import com.teamwable.network.dto.response.notification.ResponseNotificationsDto
import com.teamwable.network.dto.response.notification.ResponseNumberDto
import com.teamwable.network.util.BaseResponse
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Query

interface NotificationService {
    @GET("api/v1/notification/number")
    suspend fun getNumber(): BaseResponse<ResponseNumberDto>

    @PATCH("api/v1/notification-check")
    suspend fun patchCheck(): BaseResponse<Unit>

    @GET("api/v1/notifications")
    suspend fun getNotifications(
        @Query(value = "cursor") notificationId: Long = -1,
    ): BaseResponse<List<ResponseNotificationsDto>>

    @GET("api/v1/notification/info/all")
    suspend fun getInformation(
        @Query(value = "cursor") notificationId: Long = -1,
    ): BaseResponse<List<ResponseInformationDto>>
}
