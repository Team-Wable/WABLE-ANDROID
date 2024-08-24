package com.teamwable.data.repository

import androidx.paging.PagingData
import com.teamwable.model.notification.NotificationActionModel
import com.teamwable.model.notification.NotificationInformationModel
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    suspend fun getNumber(): Result<Int>
    suspend fun patchCheck(): Result<Boolean>
    fun getNotifications(): Flow<PagingData<NotificationActionModel>>
    fun getInformation(): Flow<PagingData<NotificationInformationModel>>
}
