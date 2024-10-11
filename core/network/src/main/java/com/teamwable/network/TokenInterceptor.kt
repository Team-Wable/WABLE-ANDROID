package com.teamwable.network

import android.app.Application
import android.content.Intent
import android.widget.Toast
import com.teamwable.datastore.datasource.DefaultWablePreferenceDatasource
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
    private val defaultWablePreferenceDatasource: DefaultWablePreferenceDatasource,
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

    private suspend fun refreshTokenIfNeeded(): Boolean {
        mutex.withLock {
            val accessToken = defaultWablePreferenceDatasource.accessToken.first()
            val refreshToken = defaultWablePreferenceDatasource.refreshToken.first()

            return try {
                val tokenResult = runBlocking(Dispatchers.IO) {
                    authService.getReissueToken(accessToken, refreshToken)
                }

                when (tokenResult.success) {
                    true -> {
                        defaultWablePreferenceDatasource.updateAccessToken(
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

    private fun handleFailedTokenReissue() = CoroutineScope(Dispatchers.Main).launch {
        showToast()
        withContext(Dispatchers.IO) {
            defaultWablePreferenceDatasource.clear()
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

    private fun Request.newAuthBuilder() = newBuilder()
        .addHeader(
            name = AUTHORIZATION,
            value = runBlocking {
                defaultWablePreferenceDatasource.accessToken.first()
            },
        ).build()

    companion object {
        const val CODE_TOKEN_EXPIRE = 401
        const val AUTHORIZATION = "Authorization"
        const val BEARER = "Bearer "
    }
}
