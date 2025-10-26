package com.teamwable.data.di

import com.teamwable.common.intentprovider.DailyTaskScheduler
import com.teamwable.data.worker.DailyTaskSchedulerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SchedulerModule {
    @Binds
    @Singleton
    abstract fun bindDailyTaskScheduler(
        impl: DailyTaskSchedulerImpl,
    ): DailyTaskScheduler
}
