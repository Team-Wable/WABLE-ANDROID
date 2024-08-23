package com.teamwable.data.repository

interface NotificationRepository {
    suspend fun getNumber(): Result<Int>
    suspend fun patchCheck(): Result<Boolean>
}
