package com.teamwable.data.mapper.toData

import com.teamwable.network.dto.request.RequestReportDto

internal fun Pair<String, String>.toReportDto(): RequestReportDto =
    RequestReportDto(first, second.ifEmpty { "(없음)" })
