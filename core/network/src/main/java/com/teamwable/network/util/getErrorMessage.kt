package com.teamwable.network.util

import com.teamwable.model.network.Error
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import java.net.UnknownHostException

private const val UNKNOWN_ERROR_MESSAGE = "Unknown error"

private fun HttpException.getErrorMessage(): String {
    val errorBody = response()?.errorBody()?.string() ?: return UNKNOWN_ERROR_MESSAGE
    return parseErrorMessage(errorBody)
}

private fun parseErrorMessage(errorBody: String): String {
    return try {
        val errorResponse = Json.decodeFromString<BaseResponse<Unit>>(errorBody)
        errorResponse.message
    } catch (e: Exception) {
        UNKNOWN_ERROR_MESSAGE
    }
}

fun <T> Throwable.handleThrowable(): Result<T> {
    return Result.failure(
        when (this) {
            is HttpException -> Error.ApiError(this.getErrorMessage())
            is UnknownHostException -> Error.NetWorkConnectError("인터넷에 연결해 주세요")
            else -> this
        },
    )
}
