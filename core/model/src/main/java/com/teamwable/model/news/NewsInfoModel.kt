package com.teamwable.model.news

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NewsInfoModel(
    val newsId: Long,
    val newsTitle: String,
    val newsText: String,
    val newsImage: String?,
    val time: String,
) : Parcelable
