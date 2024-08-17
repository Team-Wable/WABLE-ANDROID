package com.teamwable.model.network

data class ApiError(
    val errorMessage: String?,
) : Exception(errorMessage)
