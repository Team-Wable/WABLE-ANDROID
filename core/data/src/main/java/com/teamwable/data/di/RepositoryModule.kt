package com.teamwable.data.di

import com.teamwable.data.repository.AuthRepository
import com.teamwable.data.repository.CommentRepository
import com.teamwable.data.repository.CommunityRepository
import com.teamwable.data.repository.FeedRepository
import com.teamwable.data.repository.NewsRepository
import com.teamwable.data.repository.NotificationRepository
import com.teamwable.data.repository.PostingRepository
import com.teamwable.data.repository.ProfileRepository
import com.teamwable.data.repository.UserInfoRepository
import com.teamwable.data.repositoryimpl.DefaultAuthRepository
import com.teamwable.data.repositoryimpl.DefaultCommentRepository
import com.teamwable.data.repositoryimpl.DefaultCommunityRepository
import com.teamwable.data.repositoryimpl.DefaultFeedRepository
import com.teamwable.data.repositoryimpl.DefaultNewsRepository
import com.teamwable.data.repositoryimpl.DefaultNotificationRepository
import com.teamwable.data.repositoryimpl.DefaultPostingRepository
import com.teamwable.data.repositoryimpl.DefaultProfileRepository
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
    abstract fun bindsWableLocalDataSource(
        dataSourceImpl: DefaultWablePreferenceDatasource,
    ): WablePreferencesDataSource

    @Binds
    @Singleton
    abstract fun bindsUserInfoRepository(
        repositoryImpl: DefaultUserInfoRepository,
    ): UserInfoRepository

    @Binds
    @Singleton
    abstract fun bindsAuthRepository(
        repositoryImpl: DefaultAuthRepository,
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindsNewsRepository(repositoryImpl: DefaultNewsRepository): NewsRepository

    @Binds
    @Singleton
    abstract fun bindsNotificationRepository(repositoryImpl: DefaultNotificationRepository): NotificationRepository

    @Binds
    @Singleton
    abstract fun bindsFeedRepository(
        repositoryImpl: DefaultFeedRepository,
    ): FeedRepository

    @Binds
    @Singleton
    abstract fun bindsProfileRepository(
        repositoryImpl: DefaultProfileRepository,
    ): ProfileRepository

    @Binds
    @Singleton
    abstract fun bindsCommentRepository(
        repositoryImpl: DefaultCommentRepository,
    ): CommentRepository

    @Binds
    @Singleton
    abstract fun bindsPostingRepository(
        repositoryImpl: DefaultPostingRepository,
    ): PostingRepository

    @Binds
    @Singleton
    abstract fun bindsCommunityRepository(
        repositoryImpl: DefaultCommunityRepository,
    ): CommunityRepository
}
