package com.teamwable.data.di

import com.teamwable.data.remote.datasource.DummyService
import com.teamwable.data.repository.DummyRepository
import com.teamwable.data.repositoryimpl.DefaultDummyRepository
import com.teamwable.network.WableRetrofit
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DummyModule {

    @Singleton
    @Provides
    fun provideDummyService(
        @WableRetrofit retrofit: Retrofit,
    ): DummyService = retrofit.create(DummyService::class.java)

    @Module
    @InstallIn(SingletonComponent::class)
    interface RepositoryModule {
        @Binds
        @Singleton
        fun bindsHomeRepository(repositoryImpl: DefaultDummyRepository): DummyRepository
    }
}