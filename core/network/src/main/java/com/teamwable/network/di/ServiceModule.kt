package com.teamwable.network.di

import com.teamwable.network.datasource.AuthService
import com.teamwable.network.datasource.CommentService
import com.teamwable.network.datasource.DummyService
import com.teamwable.network.datasource.FeedService
import com.teamwable.network.datasource.NewsService
import com.teamwable.network.datasource.NotificationService
import com.teamwable.network.datasource.PostingService
import com.teamwable.network.datasource.ProfileService
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
    fun provideNewsService(
        @WableRetrofit retrofit: Retrofit,
    ): NewsService = retrofit.create(NewsService::class.java)

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

    @Singleton
    @Provides
    fun provideCommentService(
        @WableRetrofit retrofit: Retrofit,
    ): CommentService = retrofit.create(CommentService::class.java)

    @Singleton
    @Provides
    fun providePostingService(
        @WableRetrofit retrofit: Retrofit,
    ): PostingService = retrofit.create(PostingService::class.java)
}
