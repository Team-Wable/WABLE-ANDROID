package com.teamwable.model.network

sealed class WableError(message: String?) : Exception(message) {
    data class ApiError(
        val errorMessage: String?,
    ) : WableError(errorMessage)

    data class NetWorkConnectError(
        val errorMessage: String,
    ) : WableError(errorMessage)

    data class TimeOutError(
        val errorMessage: String,
    ) : WableError(errorMessage)

    data class CustomError(
        val errorMessage: String,
    ) : WableError(errorMessage)
}
