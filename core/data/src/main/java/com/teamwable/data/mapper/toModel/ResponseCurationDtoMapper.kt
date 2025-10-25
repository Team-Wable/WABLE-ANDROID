package com.teamwable.data.mapper.toModel

import com.teamwable.model.news.CurationModel
import com.teamwable.network.dto.response.news.ResponseCurationInfoDto

internal fun ResponseCurationInfoDto.toCuration(): CurationModel =
    CurationModel(
        this.curationId,
        this.curationLink,
        this.curationTitle ?: "",
        this.curationThumbnail ?: "",
        this.time,
    )
