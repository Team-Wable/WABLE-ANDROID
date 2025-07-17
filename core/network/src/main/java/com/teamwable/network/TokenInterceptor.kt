package com.teamwable.network

import com.teamwable.datastore.datasource.WablePreferencesDataSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class TokenInterceptor @Inject constructor(
    private val dataStore: WablePreferencesDataSource,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = runBlocking { dataStore.accessToken.first() }
        val authRequest: Request = chain.request().newBuilder()
            .addHeader("Authorization", accessToken)
            .build()

        return chain.proceed(authRequest)
    }
}
