package com.teamwable.data.di

import com.teamwable.data.repository.DummyRepository
import com.teamwable.data.repositoryimpl.DefaultDummyRepository
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
}
