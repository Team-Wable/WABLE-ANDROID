package com.teamwable.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//TODO:BaseResponse 수정(명세서 나온 뒤)
@Serializable
data class BaseResponse<T>(
    @SerialName("status")
    val status: Int,
    @SerialName("success")
    val success: Boolean,
    @SerialName("message")
    val message: String,
    @SerialName("data")
    val data: T? = null,
)