package com.teamwable.news.detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.model.news.NewsInfoModel
import com.teamwable.news.NewsTabType

@Composable
fun NewsDetailRoute(
    newsInfoModel: NewsInfoModel,
    type: NewsTabType,
    navigateToImageDetail: () -> Unit = {},
    navigateToBack: () -> Unit,
) {
    NewsDetailScreen(
        newsInfoModel = newsInfoModel,
        type = type,
        navigateToImageDetail = navigateToImageDetail,
        navigateToBack = navigateToBack,
    )
}

@Composable
fun NewsDetailScreen(
    newsInfoModel: NewsInfoModel,
    type: NewsTabType,
    navigateToImageDetail: () -> Unit = {},
    navigateToBack: () -> Unit,
) {
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WableTheme {
        NewsDetailScreen(
            newsInfoModel = NewsInfoModel(
                newsId = 1,
                newsImage = "",
                newsText = "",
                newsTitle = "",
                time = "",
            ),
            type = NewsTabType.NEWS,
            navigateToBack = {},
        )
    }
}
