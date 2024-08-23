package com.teamwable.network.di

import com.teamwable.network.datasource.AuthService
import com.teamwable.network.datasource.DummyService
import com.teamwable.network.datasource.FeedService
import com.teamwable.network.datasource.ProfileService
import com.teamwable.network.datasource.NotificationService
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
    fun provideNotificationService(
        @WableRetrofit retrofit: Retrofit,
    ): NotificationService = retrofit.create(NotificationService::class.java)

    @Singleton
    @Provides
    fun provideFeedService(
        @WableRetrofit retrofit: Retrofit,
    ): FeedService = retrofit.create(FeedService::class.java)

    @Singleton
    @Provides
    fun provideProfileService(
        @WableRetrofit retrofit: Retrofit,
    ): ProfileService = retrofit.create(ProfileService::class.java)
}
