package com.teamwable.onboarding.profile.regex

import com.teamwable.designsystem.type.NicknameType
import javax.inject.Inject

class NicknameValidationUseCase @Inject constructor() {
    operator fun invoke(input: String): NicknameType = when {
        !isLengthValid(input) || !isOnlyAlphanumeric(input) -> NicknameType.INVALID
        else -> NicknameType.DEFAULT
    }

    private fun isOnlyAlphanumeric(input: String): Boolean =
        input.all { it.isLetterOrDigit() }

    private fun isLengthValid(input: String): Boolean =
        input.length in MIN_LENGTH..MAX_LENGTH

    companion object {
        private const val MIN_LENGTH = 1
        private const val MAX_LENGTH = 10
    }
}
