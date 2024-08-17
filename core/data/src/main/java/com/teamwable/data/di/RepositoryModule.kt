package com.teamwable.data.di

import com.teamwable.data.repository.DummyRepository
import com.teamwable.data.repository.UserInfoRepository
import com.teamwable.data.repositoryimpl.DefaultDummyRepository
import com.teamwable.data.repositoryimpl.DefaultUserInfoRepository
import com.teamwable.datastore.datasource.DefaultWablePreferenceDatasource
import com.teamwable.datastore.datasource.WablePreferencesDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindsHomeRepository(repositoryImpl: DefaultDummyRepository): DummyRepository

    @Binds
    @Singleton
    abstract fun bindsWableLocalDataSource(
        dataSourceImpl: DefaultWablePreferenceDatasource,
    ): WablePreferencesDataSource

    @Binds
    @Singleton
    abstract fun bindsUserInfoRepository(
        repositoryImpl: DefaultUserInfoRepository,
    ): UserInfoRepository
}
