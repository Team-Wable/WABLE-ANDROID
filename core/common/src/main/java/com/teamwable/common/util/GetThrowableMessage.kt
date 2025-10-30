package com.teamwable.common.util

import com.teamwable.model.network.WableError

private const val UNKNOWN_ERROR_MESSAGE = "알수 없는 오류가 발생 했습니다"

fun Throwable?.getThrowableMessage(): String = when (this) {
    is WableError -> this.message ?: UNKNOWN_ERROR_MESSAGE
    else -> UNKNOWN_ERROR_MESSAGE
}
