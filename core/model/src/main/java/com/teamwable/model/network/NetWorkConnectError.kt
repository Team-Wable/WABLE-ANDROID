package com.teamwable.model.network

data class NetWorkConnectError(
    val errorMessage: String,
) : Exception(errorMessage)
