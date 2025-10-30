package com.teamwable.data.repositoryimpl

import android.content.ContentResolver
import com.teamwable.data.mapper.toData.toBanDto
import com.teamwable.data.mapper.toData.toReportDto
import com.teamwable.data.mapper.toModel.toMemberDataModel
import com.teamwable.data.mapper.toModel.toProfile
import com.teamwable.data.repository.ProfileRepository
import com.teamwable.data.util.createImagePart
import com.teamwable.data.util.runHandledCatching
import com.teamwable.model.Profile
import com.teamwable.model.profile.MemberDataModel
import com.teamwable.model.profile.MemberInfoEditModel
import com.teamwable.network.datasource.ProfileService
import com.teamwable.network.dto.request.RequestWithdrawalDto
import com.teamwable.network.util.handleThrowable
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import javax.inject.Inject

internal class DefaultProfileRepository @Inject constructor(
    private val contentResolver: ContentResolver,
    private val apiService: ProfileService,
) : ProfileRepository {
    override suspend fun getProfileInfo(userId: Long): Result<Profile> = runCatching {
        apiService.getProfileInfo(userId).data.toProfile()
    }.onFailure {
        return it.handleThrowable()
    }

    override suspend fun getMemberData(): Result<MemberDataModel> {
        return runCatching {
            apiService.getMemberData().data.toMemberDataModel()
        }.onFailure { return it.handleThrowable() }
    }

    override suspend fun patchWithdrawal(deletedReason: List<String>): Result<Unit> {
        return runCatching {
            apiService.patchWithdrawal(RequestWithdrawalDto(deletedReason))
            Unit
        }.onFailure { return it.handleThrowable() }
    }

    override suspend fun patchUserProfile(info: MemberInfoEditModel, imgUrl: String?): Result<Unit> = runHandledCatching {
        val infoRequestBody = createContentRequestBody(info)
        val filePart = contentResolver.createImagePart(imgUrl, FILE_NAME)

        apiService.patchUserProfile(infoRequestBody, filePart)
        Unit
    }

    override suspend fun getNickNameDoubleCheck(nickname: String): Result<Unit> = runHandledCatching {
        apiService.getNickNameDoubleCheck(nickname)
        Unit
    }

    private fun createContentRequestBody(info: MemberInfoEditModel): RequestBody {
        val contentJson = JSONObject().apply {
            put("nickname", info.nickname)
            put("isAlarmAllowed", info.isAlarmAllowed)
            put("memberIntro", info.memberIntro)
            put("isPushAlarmAllowed", info.isPushAlarmAllowed)
            put("fcmToken", info.fcmToken)
            put("memberLckYears", info.memberLckYears)
            put("memberFanTeam", info.memberFanTeam)
            put("memberDefaultProfileImage", info.memberDefaultProfileImage)
        }.toString()
        return contentJson.toRequestBody("application/json".toMediaTypeOrNull())
    }

    override suspend fun postReport(nickname: String, relateText: String): Result<Unit> = runCatching {
        val request = Pair(nickname, relateText).toReportDto()
        apiService.postReport(request)
        Unit
    }.onFailure {
        return it.handleThrowable()
    }

    override suspend fun postBan(banInfo: Triple<Long, String, Long>): Result<Unit> = runCatching {
        apiService.postBan(banInfo.toBanDto())
        Unit
    }.onFailure {
        return it.handleThrowable()
    }

    companion object {
        private const val FILE_NAME = "file"
    }
}
