package com.teamwable.data.worker

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.teamwable.common.intentprovider.DailyTaskScheduler
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DailyTaskSchedulerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : DailyTaskScheduler {
    override fun scheduleDailyReset() {
        val now = LocalDateTime.now()
        val nextMidnight = LocalDateTime.now().plusDays(1).with(LocalTime.MIDNIGHT)
        val initialDelay = Duration.between(now, nextMidnight)

        val dailyResetRequest = PeriodicWorkRequestBuilder<ResetQuizStatusWorker>(
            repeatInterval = 24,
            repeatIntervalTimeUnit = TimeUnit.HOURS,
        )
            .setInitialDelay(initialDelay)
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 1, TimeUnit.MINUTES)
            .build()

        WorkManager.Companion.getInstance(context).enqueueUniquePeriodicWork(
            ResetQuizStatusWorker.Companion.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            dailyResetRequest,
        )
    }
}
