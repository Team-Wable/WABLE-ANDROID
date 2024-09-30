package com.teamwable.main.di

import com.teamwable.common.di.MAIN
import com.teamwable.common.intentprovider.IntentProvider
import com.teamwable.main.intentprovider.DefaultIntentProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class IntentModule {
    @Binds
    @Singleton
    @MAIN
    abstract fun bindsIntentProvider(intentProvider: DefaultIntentProvider): IntentProvider
}
