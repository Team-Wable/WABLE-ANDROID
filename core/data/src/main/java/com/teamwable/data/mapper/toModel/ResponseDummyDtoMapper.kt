package com.teamwable.data.mapper.toModel

import com.teamwable.data.remote.dto.response.ResponseDummyDto
import com.teamwable.model.Dummy

fun ResponseDummyDto.toDummy(): Dummy = Dummy(
    this.contentText,
)