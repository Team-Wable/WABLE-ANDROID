package com.teamwable.news.news.contract

import com.teamwable.common.base.BaseIntent
import com.teamwable.model.news.NewsInfoModel

sealed interface NewsInfoIntent : BaseIntent {
    data class ItemClick(val newsInfoModel: NewsInfoModel) : NewsInfoIntent
}
