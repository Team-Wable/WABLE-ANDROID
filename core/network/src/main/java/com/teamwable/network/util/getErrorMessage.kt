package com.teamwable.network.util

import com.teamwable.model.network.WableError
import org.json.JSONObject
import retrofit2.HttpException
import timber.log.Timber
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

private const val UNKNOWN_ERROR_MESSAGE = "Unknown error"
private const val NETWORK_CONNECT_ERROR_MESSAGE = "네트워크 연결이 원활하지 않습니다"
private const val SERVER_TIMEOUT_ERROR_MESSAGE = "서버가 응답하지 않습니다"

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

fun Throwable.toCustomError(): Throwable {
    Timber.e(this)
    return when (this) {
        is HttpException -> WableError.ApiError(this.getErrorMessage())
        is UnknownHostException -> WableError.NetWorkConnectError(NETWORK_CONNECT_ERROR_MESSAGE)
        is ConnectException -> WableError.NetWorkConnectError(NETWORK_CONNECT_ERROR_MESSAGE)
        is SocketException -> WableError.NetWorkConnectError(NETWORK_CONNECT_ERROR_MESSAGE)
        is SocketTimeoutException -> WableError.TimeOutError(SERVER_TIMEOUT_ERROR_MESSAGE)
        else -> this
    }
}

fun <T> Throwable.handleThrowable(): Result<T> = Result.failure(this.toCustomError())
