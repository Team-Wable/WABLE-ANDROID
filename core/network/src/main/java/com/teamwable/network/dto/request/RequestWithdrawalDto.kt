package com.teamwable.network.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestWithdrawalDto(
    @SerialName("deleted_reason")
    val deletedReason: List<String>
)
