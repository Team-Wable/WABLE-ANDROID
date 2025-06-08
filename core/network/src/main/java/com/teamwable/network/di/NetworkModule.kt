package com.teamwable.network.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.teamwable.network.BuildConfig.WABLE_BASE_URL
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
    /**
     * Provides a singleton instance of Json configured for pretty printing, lenient parsing, and ignoring unknown keys.
     *
     * @return A configured Json instance for serialization and deserialization.
     */
    @Provides
    @Singleton
    fun providesJson(): Json = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
    }

    /**
             * Provides a singleton OkHttpClient configured with token and logging interceptors, and 5-second connection and read timeouts.
             *
             * @return An OkHttpClient instance for network requests requiring authentication and logging.
             */
            @Singleton
    @Provides
    fun provideOkHttpClient(
        @AccessToken tokenInterceptor: Interceptor,
        loggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .addInterceptor(tokenInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()

    @Provides
    @Singleton
    @AccessToken
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

    /**
     * Provides a singleton Retrofit instance configured with the base URL, the given OkHttpClient, and a JSON converter using the provided Json instance.
     *
     * @return A Retrofit instance for network API calls.
     */
    @Singleton
    @Provides
    @WableRetrofit
    fun provideRetrofit(okHttpClient: OkHttpClient, json: Json): Retrofit {
        val build =
            Retrofit.Builder()
                .baseUrl(WABLE_BASE_URL)
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

    /**
             * Provides a singleton Retrofit instance without a token interceptor, using the specified OkHttpClient and Json converter.
             *
             * The returned Retrofit is annotated with @WithoutTokenInterceptor and uses the base URL defined by WABLE_BASE_URL.
             *
             * @return A Retrofit instance configured for requests that do not require token authentication.
             */
            @Provides
    @Singleton
    @WithoutTokenInterceptor
    fun provideRetrofitWithoutTokenInterceptor(@WithoutTokenInterceptor okHttpClient: OkHttpClient, json: Json): Retrofit =
        Retrofit.Builder()
            .baseUrl(WABLE_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
}
