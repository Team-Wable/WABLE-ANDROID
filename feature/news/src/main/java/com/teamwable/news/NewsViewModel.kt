package com.teamwable.news

import androidx.lifecycle.ViewModel
import com.teamwable.model.NewsMatchModel
import com.teamwable.model.NewsRankModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewsViewModel
@Inject constructor() : ViewModel() {
    val mockNewsMatchList = listOf(
        NewsMatchModel("08. 17 (토)", "T1", 2, "GEN", 3, "종료"),
        NewsMatchModel("08. 17 (토)", "FOX", 1, "BRO", 0, "진행중"),
        NewsMatchModel("08. 19 (월)", "T1", 2, "GEN", 3, "예정"),
    )

    val mockNewsRankList = listOf(
        NewsRankModel(1, "T1", 8, 7, 8, 0),
        NewsRankModel(2, "GEN", 8, 7, 8, 0),
        NewsRankModel(3, "BRO", 8, 7, 8, 0),
        NewsRankModel(4, "DRX", 8, 7, 8, 0),
        NewsRankModel(5, "DK", 8, 7, 8, 0),
        NewsRankModel(6, "KT", 8, 7, 8, 0),
        NewsRankModel(7, "FOX", 8, 7, 8, 0),
        NewsRankModel(8, "NS", 8, 7, 8, 0),
        NewsRankModel(9, "T1", 8, 7, 8, 0),
        NewsRankModel(10, "GEN", 8, 7, 8, 0),
        NewsRankModel(11, "BRO", 8, 7, 8, 0),
        NewsRankModel(12, "DRX", 8, 7, 8, 0),
        NewsRankModel(13, "DK", 8, 7, 8, 0),
    )
}
