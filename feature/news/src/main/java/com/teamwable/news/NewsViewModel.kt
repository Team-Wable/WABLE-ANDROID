package com.teamwable.news

import androidx.lifecycle.ViewModel
import com.teamwable.model.NewsMatchModel
import com.teamwable.model.NewsMatchScoreModel
import com.teamwable.model.NewsRankModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewsViewModel
@Inject constructor() : ViewModel() {
    val mockNewsMatchList = listOf(
        NewsMatchModel(
            "2024-08-17",
            listOf(
                NewsMatchScoreModel("2024-08-17 16:07", "T1", 2, "GEN", 3, "TERMINATION"),
                NewsMatchScoreModel("2024-08-17 20:00", "FOX", 1, "BRO", 0, "PROGRESS"),
            )
        ),
        NewsMatchModel(
            "2024-08-20",
            listOf(NewsMatchScoreModel("2024-08-20 09:50", "T1", 2, "GEN", 3, "SCHEDULED"))
        ),
        NewsMatchModel(
            "2024-08-21",
            listOf(NewsMatchScoreModel("2024-08-21 12:25", "T1", 2, "GEN", 3, "SCHEDULED"))
        ),
    )
    val mockNewsMatchEmptyList = emptyList<NewsMatchModel>()

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
