package com.teamwable.data.repository

import kotlinx.coroutines.flow.Flow

interface UserInfoRepository {
    fun getAccessToken(): Flow<String>

    fun getRefreshToken(): Flow<String>

    fun getAutoLogin(): Flow<Boolean>

    fun getNickname(): Flow<String>

    fun getMemberId(): Flow<Int>

    fun getMemberProfileUrl(): Flow<String>

    fun getIsPushAlarmAllowed(): Flow<Boolean>

    fun getIsAdmin(): Flow<Boolean>

    fun getNewsNumber(): Flow<Int>

    fun getNoticeNumber(): Flow<Int>

    fun getQuizCompleted(): Flow<Boolean>

    suspend fun saveAccessToken(accessToken: String)

    suspend fun saveRefreshToken(refreshToken: String)

    suspend fun saveAutoLogin(autoLogin: Boolean)

    suspend fun saveNickname(name: String)

    suspend fun saveMemberId(memberId: Int)

    suspend fun saveMemberProfileUrl(memberUrl: String)

    suspend fun saveIsPushAlarmAllowed(isPushAlarmAllowed: Boolean)

    suspend fun saveIsAdmin(isAdmin: Boolean)

    suspend fun saveNewsNumber(newsNumber: Int)

    suspend fun saveNoticeNumber(noticeNumber: Int)

    suspend fun clearAll()

    suspend fun clearForRefreshToken()

    suspend fun saveQuizCompleted(isCompleted: Boolean)
}
