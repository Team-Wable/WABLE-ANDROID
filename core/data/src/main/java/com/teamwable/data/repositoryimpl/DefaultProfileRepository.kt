package com.teamwable.data.repositoryimpl

import android.content.ContentResolver
import com.teamwable.data.mapper.toModel.toMemberDataModel
import com.teamwable.data.mapper.toModel.toProfile
import com.teamwable.data.repository.ProfileRepository
import com.teamwable.model.Profile
import com.teamwable.model.profile.MemberDataModel
import com.teamwable.model.profile.MemberInfoEditModel
import com.teamwable.network.datasource.ProfileService
import com.teamwable.network.dto.request.RequestWithdrawalDto
import com.teamwable.network.util.createImagePart
import com.teamwable.network.util.handleThrowable
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import javax.inject.Inject

class DefaultProfileRepository @Inject constructor(
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

    override suspend fun patchProfileUriEdit(info: MemberInfoEditModel, file: String?): Result<Unit> {
        return runCatching {
            val infoRequestBody = createContentRequestBody(info)
            val filePart = contentResolver.createImagePart(file)

            apiService.patchUserProfile(infoRequestBody, filePart).success
        }
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
}
