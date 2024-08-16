package com.teamwable.datastore.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class DefaultWablePreferenceDatasource @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : WablePreferencesDataSource {
    private object PreferencesKeys {
        val AccessToken = stringPreferencesKey("accessToken")
        val RefreshToken = stringPreferencesKey("refreshToken")
        val Nickname = stringPreferencesKey("nickname")
        val AutoLogin = booleanPreferencesKey("autoLogin")
        val MemberId = intPreferencesKey("memberId")
        val MemberProfileUrl = stringPreferencesKey("memberProfileUrl")
        val IsNewUser = booleanPreferencesKey("isNewUser")
        val IsPushAlarmAllowed = booleanPreferencesKey("isPushAlarmAllowed")
    }

    override val accessToken: Flow<String> = dataStore.data
        .catch { handleError(it) }
        .map { preferences ->
            preferences[PreferencesKeys.AccessToken].orEmpty()
        }

    override val refreshToken: Flow<String> = dataStore.data
        .catch { handleError(it) }
        .map { preferences ->
            preferences[PreferencesKeys.RefreshToken].orEmpty()
        }

    override val autoLogin: Flow<Boolean> = dataStore.data
        .catch { handleError(it) }
        .map { preferences ->
            preferences[PreferencesKeys.AutoLogin] ?: false
        }

    override val nickname: Flow<String> = dataStore.data
        .catch { handleError(it) }
        .map { preferences ->
            preferences[PreferencesKeys.Nickname].orEmpty()
        }

    override val memberId: Flow<Int> = dataStore.data
        .catch { handleError(it) }
        .map { preferences ->
            preferences[PreferencesKeys.MemberId] ?: -1
        }

    override var memberProfileUrl: Flow<String> = dataStore.data
        .catch { handleError(it) }
        .map { preferences ->
            preferences[PreferencesKeys.MemberProfileUrl].orEmpty()
        }

    override var isNewUser: Flow<Boolean> = dataStore.data
        .catch { handleError(it) }
        .map { preferences ->
            preferences[PreferencesKeys.IsNewUser] ?: false
        }

    override var isPushAlarmAllowed: Flow<Boolean> = dataStore.data
        .catch { handleError(it) }
        .map { preferences ->
            preferences[PreferencesKeys.IsPushAlarmAllowed] ?: false
        }

    override suspend fun updateAccessToken(accessToken: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.AccessToken] = accessToken
        }
    }

    override suspend fun updateRefreshToken(refreshToken: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.RefreshToken] = refreshToken
        }
    }

    override suspend fun updateAutoLogin(autoLogin: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.AutoLogin] = autoLogin
        }
    }

    override suspend fun updateNickname(name: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.Nickname] = name
        }
    }

    override suspend fun updateMemberId(memberId: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.MemberId] = memberId
        }
    }

    override suspend fun updateMemberProfileUrl(memberUrl: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.MemberProfileUrl] = memberUrl
        }
    }

    override suspend fun updateIsNewUser(isNewUser: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.IsNewUser] = isNewUser
        }
    }

    override suspend fun updateIsPushAlarmAllowed(isPushAlarmAllowed: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.IsPushAlarmAllowed] = isPushAlarmAllowed
        }
    }

    override suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    override suspend fun clearForRefreshToken() {
        dataStore.edit { preferences ->
            preferences.remove(PreferencesKeys.AccessToken)
            preferences.remove(PreferencesKeys.RefreshToken)
            preferences.remove(PreferencesKeys.AutoLogin)
        }
    }
}

private suspend fun FlowCollector<Preferences>.handleError(it: Throwable) {
    if (it is IOException) emit(emptyPreferences())
    else throw it
}
