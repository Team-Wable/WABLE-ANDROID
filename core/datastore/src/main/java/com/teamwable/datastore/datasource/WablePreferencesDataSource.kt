package com.teamwable.datastore.datasource

import kotlinx.coroutines.flow.Flow

interface WablePreferencesDataSource {
    val accessToken: Flow<String>
    val refreshToken: Flow<String>
    val autoLogin: Flow<Boolean>
    val nickname: Flow<String>
    val memberId: Flow<Int>
    val memberProfileUrl: Flow<String>
    val isNewUser: Flow<Boolean>
    val isPushAlarmAllowed: Flow<Boolean>

    suspend fun updateAccessToken(accessToken: String)

    suspend fun updateRefreshToken(refreshToken: String)

    suspend fun updateAutoLogin(autoLogin: Boolean)

    suspend fun updateNickname(name: String)

    suspend fun updateMemberId(memberId: Int)

    suspend fun updateMemberProfileUrl(memberUrl: String)

    suspend fun updateIsNewUser(isNewUser: Boolean)

    suspend fun updateIsPushAlarmAllowed(isPushAlarmAllowed: Boolean)

    suspend fun clear()

    suspend fun clearForRefreshToken()
}
