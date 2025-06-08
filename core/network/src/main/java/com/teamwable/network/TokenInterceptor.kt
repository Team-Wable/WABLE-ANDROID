package com.teamwable.network

import android.app.Application
import android.content.Intent
import android.widget.Toast
import com.teamwable.datastore.datasource.WablePreferencesDataSource
import com.teamwable.network.datasource.AuthService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenInterceptor @Inject constructor(
    private val wablePreferencesDataSource: WablePreferencesDataSource,
    private val context: Application,
    private val authService: AuthService,
) : Interceptor {
    private val mutex = Mutex()
    private var currentToast: Toast? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        var response = chain.proceed(originalRequest.newAuthBuilder())

        if (response.code == CODE_TOKEN_EXPIRE) {
            response.close()
            val tokenRefreshed = runBlocking {
                refreshTokenIfNeeded()
            }
            if (tokenRefreshed) {
                response = chain.proceed(originalRequest.newAuthBuilder())
            } else {
                handleFailedTokenReissue()
            }
        }
        return response
    }

    /**
     * Attempts to refresh the access token using the stored refresh token if needed.
     *
     * Acquires a mutex lock to ensure only one refresh operation occurs at a time.
     * Updates the stored access token on successful refresh.
     *
     * @return `true` if the token was refreshed successfully, `false` otherwise.
     */
    private suspend fun refreshTokenIfNeeded(): Boolean {
        mutex.withLock {
            val accessToken = wablePreferencesDataSource.accessToken.first()
            val refreshToken = wablePreferencesDataSource.refreshToken.first()

            return try {
                val tokenResult = runBlocking(Dispatchers.IO) {
                    authService.getReissueToken(accessToken, refreshToken)
                }

                when (tokenResult.success) {
                    true -> {
                        wablePreferencesDataSource.updateAccessToken(
                            BEARER + tokenResult.data.accessToken,
                        )
                        true
                    }

                    false -> false
                }
            } catch (e: Exception) {
                false
            }
        }
    }

    /**
     * Handles actions required when token reissue fails by notifying the user, clearing stored tokens, and restarting the application.
     */
    private fun handleFailedTokenReissue() = CoroutineScope(Dispatchers.Main).launch {
        showToast()
        withContext(Dispatchers.IO) {
            wablePreferencesDataSource.clear()
        }
        restartActivity()
    }

    private fun showToast() {
        currentToast?.cancel()
        currentToast = Toast.makeText(context, "재 로그인이 필요해요", Toast.LENGTH_SHORT)
        currentToast?.show()
    }

    private suspend fun restartActivity() = with(context) {
        mutex.withLock {
            startActivity(
                Intent.makeRestartActivityTask(
                    packageManager.getLaunchIntentForPackage(packageName)?.component,
                ),
            )
        }
    }

    /**
         * Creates a new HTTP request with the current access token added to the Authorization header.
         *
         * Retrieves the access token synchronously and attaches it as a Bearer token.
         * @return A new [Request] instance with the Authorization header set.
         */
        private fun Request.newAuthBuilder() = newBuilder()
        .addHeader(
            name = AUTHORIZATION,
            value = runBlocking {
                wablePreferencesDataSource.accessToken.first()
            },
        ).build()

    companion object {
        const val CODE_TOKEN_EXPIRE = 401
        const val AUTHORIZATION = "Authorization"
        const val BEARER = "Bearer "
    }
}
