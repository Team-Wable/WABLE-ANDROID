package com.teamwable.network.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class WableRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class WithoutTokenInterceptor
