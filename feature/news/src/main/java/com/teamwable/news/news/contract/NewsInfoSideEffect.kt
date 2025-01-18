package com.teamwable.news.news.contract

import com.teamwable.common.base.SideEffect
import com.teamwable.model.news.NewsInfoModel

sealed interface NewsInfoSideEffect : SideEffect {
    data class NavigateToDetail(val newsInfoModel: NewsInfoModel) : NewsInfoSideEffect

    data class ShowSnackBar(val message: String) : NewsInfoSideEffect
}
