package com.teamwable.news.news.model

import com.teamwable.model.news.NewsInfoModel

sealed interface NewsInfoSideEffect {
    data class NavigateToDetail(val newsInfoModel: NewsInfoModel) : NewsInfoSideEffect

    data class ShowSnackBar(val message: String) : NewsInfoSideEffect
}
