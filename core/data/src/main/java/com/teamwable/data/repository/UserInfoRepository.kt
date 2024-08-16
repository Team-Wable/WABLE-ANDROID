package com.teamwable.data.repository

import kotlinx.coroutines.flow.Flow

interface UserInfoRepository {
    fun getAccessToken(): Flow<String>

    fun getRefreshToken(): Flow<String>

    fun getAutoLogin(): Flow<Boolean>

    fun getNickname(): Flow<String>

    fun getMemberId(): Flow<Int>

    fun getMemberProfileUrl(): Flow<String>

    fun getIsNewUser(): Flow<Boolean>

    fun getIsPushAlarmAllowed(): Flow<Boolean>

    suspend fun saveAccessToken(accessToken: String)

    suspend fun saveRefreshToken(refreshToken: String)

    suspend fun saveAutoLogin(autoLogin: Boolean)

    suspend fun saveNickname(name: String)

    suspend fun saveMemberId(memberId: Int)

    suspend fun saveMemberProfileUrl(memberUrl: String)

    suspend fun saveIsNewUser(isNewUser: Boolean)

    suspend fun saveIsPushAlarmAllowed(isPushAlarmAllowed: Boolean)

    suspend fun clearAll()

    suspend fun clearForRefreshToken()
}
