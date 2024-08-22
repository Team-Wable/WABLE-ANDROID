package com.teamwable.network.di

import com.teamwable.network.datasource.AuthService
import com.teamwable.network.datasource.DummyService
import com.teamwable.network.datasource.FeedService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object ServiceModule {
    @Singleton
    @Provides
    fun provideDummyService(
        @WableRetrofit retrofit: Retrofit,
    ): DummyService = retrofit.create(DummyService::class.java)

    @Singleton
    @Provides
    fun provideAuthService(
        @WithoutTokenInterceptor retrofit: Retrofit,
    ): AuthService = retrofit.create(AuthService::class.java)

    @Singleton
    @Provides
    fun provideFeedService(
        @WableRetrofit retrofit: Retrofit,
    ): FeedService = retrofit.create(FeedService::class.java)
}
