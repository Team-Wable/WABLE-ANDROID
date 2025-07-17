package com.teamwable.network

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.teamwable.datastore.datasource.WablePreferencesDataSource
import com.teamwable.network.datasource.AuthService
import com.teamwable.network.util.runSuspendCatching
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
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
    @ApplicationContext private val context: Context,
) : Authenticator {
    private val mutex = Mutex()
    private var currentToast: Toast? = null

    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.code != 401) return null

        return runBlocking {
            val newAccessToken = refreshToken() ?: return@runBlocking null
            response.request
                .newBuilder()
                .header("Authorization", newAccessToken)
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
            }.getOrNull()?.let { "Bearer ${it.data.accessToken}" }
        }
    }

    private fun notifyReLoginRequired() {
        CoroutineScope(Dispatchers.Main).launch {
            showToast("재 로그인이 필요해요")
            restartApp()
        }
    }

    private fun showToast(message: String) {
        currentToast?.cancel()
        currentToast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
        currentToast?.show()
    }

    private fun restartApp() {
        val restartIntent = context.packageManager
            .getLaunchIntentForPackage(context.packageName)
            ?.component
            ?.let(Intent::makeRestartActivityTask)

        context.startActivity(restartIntent)
    }
}
