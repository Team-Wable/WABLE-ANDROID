package com.teamwable.data.repositoryimpl

import com.teamwable.data.repository.NotificationRepository
import com.teamwable.network.datasource.NotificationService
import com.teamwable.network.util.handleThrowable
import javax.inject.Inject

internal class DefaultNotificationRepository @Inject constructor(
    private val notificationService: NotificationService,
) : NotificationRepository {
    override suspend fun getNumber(): Result<Int> {
        return runCatching {
            notificationService.getNumber().data.notificationNumber
        }.onFailure { return it.handleThrowable() }
    }

    override suspend fun patchCheck(): Result<Boolean> {
        return runCatching {
            notificationService.patchCheck().success
        }.onFailure { return it.handleThrowable() }
    }
}
