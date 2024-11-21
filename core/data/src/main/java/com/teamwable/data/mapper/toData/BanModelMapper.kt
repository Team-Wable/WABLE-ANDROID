package com.teamwable.data.mapper.toData

import com.teamwable.network.dto.request.RequestBanDto

internal fun Triple<Long, String, Long>.toBanDto(): RequestBanDto =
    RequestBanDto(first, second, third)
