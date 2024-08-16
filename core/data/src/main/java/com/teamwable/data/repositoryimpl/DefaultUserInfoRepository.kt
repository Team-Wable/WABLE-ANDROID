package com.teamwable.data.repositoryimpl

import com.teamwable.data.repository.UserInfoRepository
import com.teamwable.datastore.datasource.WablePreferencesDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class DefaultUserInfoRepository @Inject constructor(
    private val wablePreferencesDataSource: WablePreferencesDataSource,
) : UserInfoRepository {
    override fun getAccessToken(): Flow<String> =
        wablePreferencesDataSource.accessToken

    override fun getRefreshToken(): Flow<String> =
        wablePreferencesDataSource.refreshToken

    override fun getAutoLogin(): Flow<Boolean> =
        wablePreferencesDataSource.autoLogin

    override fun getNickname(): Flow<String> =
        wablePreferencesDataSource.nickname

    override fun getMemberId(): Flow<Int> =
        wablePreferencesDataSource.memberId

    override fun getMemberProfileUrl(): Flow<String> =
        wablePreferencesDataSource.memberProfileUrl

    override fun getIsNewUser(): Flow<Boolean> =
        wablePreferencesDataSource.isNewUser

    override fun getIsPushAlarmAllowed(): Flow<Boolean> =
        wablePreferencesDataSource.isPushAlarmAllowed

    override suspend fun saveAccessToken(accessToken: String) {
        wablePreferencesDataSource.updateAccessToken(accessToken)
    }

    override suspend fun saveRefreshToken(refreshToken: String) {
        wablePreferencesDataSource.updateRefreshToken(refreshToken)
    }

    override suspend fun saveAutoLogin(autoLogin: Boolean) {
        wablePreferencesDataSource.updateAutoLogin(autoLogin)
    }

    override suspend fun saveNickname(name: String) {
        wablePreferencesDataSource.updateNickname(name)
    }

    override suspend fun saveMemberId(memberId: Int) {
        wablePreferencesDataSource.updateMemberId(memberId)
    }

    override suspend fun saveMemberProfileUrl(memberUrl: String) {
        wablePreferencesDataSource.updateMemberProfileUrl(memberUrl)
    }

    override suspend fun saveIsNewUser(isNewUser: Boolean) {
        wablePreferencesDataSource.updateIsNewUser(isNewUser)
    }

    override suspend fun saveIsPushAlarmAllowed(isPushAlarmAllowed: Boolean) {
        wablePreferencesDataSource.updateIsPushAlarmAllowed(isPushAlarmAllowed)
    }

    override suspend fun clearAll() {
        wablePreferencesDataSource.clear()
    }

    override suspend fun clearForRefreshToken() {
        wablePreferencesDataSource.clearForRefreshToken()
    }
}
