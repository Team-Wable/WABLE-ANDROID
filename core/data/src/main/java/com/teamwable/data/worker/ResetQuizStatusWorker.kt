package com.teamwable.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.teamwable.data.repository.UserInfoRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber

@HiltWorker
class ResetQuizStatusWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val userInfoRepository: UserInfoRepository,
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        return try {
            userInfoRepository.saveQuizCompleted(false)
            Result.success()
        } catch (e: Exception) {
            Timber.e(e)
            Result.retry()
        }
    }

    companion object {
        const val WORK_NAME = "ResetQuizStatusWorker"
    }
}
