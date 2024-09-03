package com.teamwable.data.repositoryimpl

import com.teamwable.data.mapper.toModel.toMemberDataModel
import com.teamwable.data.mapper.toModel.toProfile
import com.teamwable.data.repository.ProfileRepository
import com.teamwable.model.Profile
import com.teamwable.model.profile.MemberDataModel
import com.teamwable.model.profile.MemberInfoEditModel
import com.teamwable.network.datasource.ProfileService
import com.teamwable.network.dto.request.RequestWithdrawalDto
import com.teamwable.network.util.handleThrowable
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File
import javax.inject.Inject

class DefaultProfileRepository @Inject constructor(
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

    override suspend fun patchProfileUriEdit(info: MemberInfoEditModel, file: File?): Result<Unit> {
        return runCatching {
            val infoJson = JSONObject().apply {
                put("nickname", info.nickname)
                put("isAlarmAllowed", info.isAlarmAllowed)
                put("memberIntro", info.memberIntro)
                put("isPushAlarmAllowed", info.isPushAlarmAllowed)
                put("fcmToken", info.fcmToken)
                put("memberLckYears", info.memberLckYears)
                put("memberFanTeam", info.memberFanTeam)
                put("memberDefaultProfileImage", info.memberDefaultProfileImage)
            }.toString()

            val infoRequestBody = infoJson.toRequestBody("application/json".toMediaTypeOrNull())

            val filePart = file?.let {
                val requestBody = it.asRequestBody("image/jpeg".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("file", it.name, requestBody)
            }

            apiService.patchUserProfile(infoRequestBody, filePart).success
        }
    }
}
