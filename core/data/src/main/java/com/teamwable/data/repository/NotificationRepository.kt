package com.teamwable.data.repository

import com.teamwable.model.NotificationInformationModel

interface NotificationRepository {
    suspend fun getNumber(): Result<Int>
    suspend fun patchCheck(): Result<Boolean>
    suspend fun getInformation(): Result<List<NotificationInformationModel>>
}
