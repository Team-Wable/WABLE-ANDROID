package com.teamwable.network

import com.teamwable.common.restarter.AppReStarter
import com.teamwable.datastore.datasource.WablePreferencesDataSource
import com.teamwable.network.datasource.AuthService
import com.teamwable.network.util.runSuspendCatching
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import timber.log.Timber
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    private val dataStore: WablePreferencesDataSource,
    private val authService: AuthService,
    private val appRestarter: AppReStarter,
) : Authenticator {
    private val mutex = Mutex()

    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.code != 401) return null

        if (response.request.header("Authorization-Retry") != null) return null

        return runBlocking {
            val newAccessToken = refreshToken() ?: return@runBlocking null
            response.request
                .newBuilder()
                .header("Authorization", newAccessToken)
                .header("Authorization-Retry", "true")
                .build()
        }
    }

    private suspend fun refreshToken(): String? {
        return mutex.withLock {
            val accessToken = dataStore.accessToken.first()
            val refreshToken = dataStore.refreshToken.first()

            runSuspendCatching {
                authService.getReissueToken(accessToken, refreshToken)
            }.onSuccess {
                val newAccess = "Bearer ${it.data.accessToken}"
                dataStore.updateAccessToken(newAccess)
                dataStore.updateRefreshToken(it.data.refreshToken)
                return newAccess
            }.onFailure {
                Timber.e(it)
                dataStore.clear()
                notifyReLoginRequired()
            }
            null
        }
    }

    private fun notifyReLoginRequired() {
        appRestarter.makeToast("재 로그인이 필요해요")
        appRestarter.restartApp()
    }
}
