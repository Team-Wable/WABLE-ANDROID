package com.teamwable.news.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NewsInfoModel(
    val newsId: Int,
    val newsTitle: String,
    val newsText: String,
    val newsImage: String?,
    val time: String,
) : Parcelable
