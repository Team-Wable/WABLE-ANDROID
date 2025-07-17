package com.teamwable.network.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.teamwable.network.BuildConfig
import com.teamwable.network.TokenAuthenticator
import com.teamwable.network.TokenInterceptor
import com.teamwable.network.util.isJsonArray
import com.teamwable.network.util.isJsonObject
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Retrofit
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {
    @Provides
    @Singleton
    fun providesJson(): Json = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        tokenInterceptor: Interceptor,
        loggingInterceptor: HttpLoggingInterceptor,
        tokenAuthenticator: TokenAuthenticator,
    ): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .addInterceptor(tokenInterceptor)
            .addInterceptor(loggingInterceptor)
            .authenticator(tokenAuthenticator)
            .build()

    @Provides
    @Singleton
    fun provideAuthInterceptor(interceptor: TokenInterceptor): Interceptor = interceptor

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor =
            HttpLoggingInterceptor { message ->
                when {
                    message.isJsonObject() -> Timber.d(JSONObject(message).toString(4))
                    message.isJsonArray() -> Timber.d(JSONArray(message).toString(4))
                    else -> Timber.d("CONNECTION INFO -> $message")
                }
            }
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }

    @Singleton
    @Provides
    @WableRetrofit
    fun provideRetrofit(okHttpClient: OkHttpClient, json: Json): Retrofit {
        val build =
            Retrofit.Builder()
                .baseUrl(BuildConfig.WABLE_BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                .build()
        return build
    }

    @Provides
    @Singleton
    @WithoutTokenInterceptor
    fun provideOkHttpClientWithoutTokenInterceptor(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

    @Provides
    @Singleton
    @WithoutTokenInterceptor
    fun provideRetrofitWithoutTokenInterceptor(@WithoutTokenInterceptor okHttpClient: OkHttpClient, json: Json): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.WABLE_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
}
