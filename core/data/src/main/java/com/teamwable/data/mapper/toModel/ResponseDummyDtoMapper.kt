package com.teamwable.data.mapper.toModel

import com.teamwable.network.dto.response.ResponseDummyDto
import com.teamwable.model.Dummy

internal fun ResponseDummyDto.toDummy(): Dummy =
    Dummy(
        this.contentText,
    )
