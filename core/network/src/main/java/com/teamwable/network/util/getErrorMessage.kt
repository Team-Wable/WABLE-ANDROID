package com.teamwable.network.util

import com.teamwable.model.network.Error
import org.json.JSONObject
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

const val UNKNOWN_ERROR_MESSAGE = "Unknown error"

private fun HttpException.getErrorMessage(): String {
    val errorBody = response()?.errorBody()?.string() ?: return UNKNOWN_ERROR_MESSAGE
    return parseErrorMessage(errorBody)
}

private fun parseErrorMessage(errorBody: String): String {
    return try {
        JSONObject(errorBody).getString("message")
    } catch (e: Exception) {
        UNKNOWN_ERROR_MESSAGE
    }
}

fun Throwable.toCustomError(): Throwable = when (this) {
    is HttpException -> Error.ApiError(this.getErrorMessage())
    is UnknownHostException -> Error.NetWorkConnectError("네트워크 연결이 원활하지 않습니다")
    is ConnectException -> Error.NetWorkConnectError("인터넷에 연결해 주세요")
    is SocketTimeoutException -> Error.TimeOutError("서버가 응답하지 않습니다")
    else -> Error.UnknownError(this.message ?: UNKNOWN_ERROR_MESSAGE)
}

fun <T> Throwable.handleThrowable(): Result<T> {
    return Result.failure(this.toCustomError())
}
