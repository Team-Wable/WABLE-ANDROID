package com.teamwable.domain.usecase

import com.teamwable.data.repository.UserInfoRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetAuthTypeUseCase @Inject constructor(
    private val userInfoRepository: UserInfoRepository,
) {
    suspend operator fun invoke(targetUserId: Long): String {
        val authId = userInfoRepository.getMemberId().first().toLong()
        val isAdmin = userInfoRepository.getIsAdmin().first()

        return when {
            targetUserId == authId -> "AUTH"
            isAdmin -> "ADMIN"
            else -> "MEMBER"
        }
    }
}
