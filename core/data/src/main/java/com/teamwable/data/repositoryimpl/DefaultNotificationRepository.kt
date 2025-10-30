package com.teamwable.data.repositoryimpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.teamwable.data.mapper.toModel.toNotificationActionModel
import com.teamwable.data.mapper.toModel.toNotificationInformationModel
import com.teamwable.data.repository.NotificationRepository
import com.teamwable.model.notification.NotificationActionModel
import com.teamwable.model.notification.NotificationInformationModel
import com.teamwable.network.datasource.NotificationService
import com.teamwable.network.util.GenericPagingSource
import com.teamwable.network.util.handleThrowable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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

    override fun getNotifications(): Flow<PagingData<NotificationActionModel>> {
        return Pager(PagingConfig(pageSize = 15, prefetchDistance = 1)) {
            GenericPagingSource(
                apiCall = { cursor -> notificationService.getNotifications(cursor).data },
                getNextCursor = { notifications -> notifications.lastOrNull()?.notificationId },
            )
        }.flow.map { pagingData ->
            pagingData.map { it.toNotificationActionModel() }
        }
    }

    override fun getInformation(): Flow<PagingData<NotificationInformationModel>> {
        return Pager(PagingConfig(pageSize = 15, prefetchDistance = 1)) {
            GenericPagingSource(
                apiCall = { cursor -> notificationService.getInformation(cursor).data },
                getNextCursor = { information -> information.lastOrNull()?.infoNotificationId },
            )
        }.flow.map { pagingData ->
            pagingData.map { it.toNotificationInformationModel() }
        }
    }
}
