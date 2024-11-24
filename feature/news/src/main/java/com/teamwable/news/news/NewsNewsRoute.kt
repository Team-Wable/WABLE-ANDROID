package com.teamwable.news.news

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.model.news.NewsInfoModel
import com.teamwable.news.news.component.WableNewsItems
import com.teamwable.news.news.component.WableNewsTopBanner

@Composable
fun NewsNewsRoute(
    navigateToDetail: (NewsInfoModel) -> Unit,
) {
    NewsNewsScreen(navigateToDetail)
}

@Composable
fun NewsNewsScreen(
    navigateToProfile: (NewsInfoModel) -> Unit,
) {
    LazyColumn(
        contentPadding = PaddingValues(vertical = 4.dp),
        modifier = Modifier.fillMaxSize(),
    ) {
        item {
            WableNewsTopBanner(
                modifier = Modifier.padding(
                    top = 13.dp,
                    start = dimensionResource(com.teamwable.common.R.dimen.padding_horizontal),
                    end = dimensionResource(com.teamwable.common.R.dimen.padding_horizontal),
                ),
            ) {
                Column {
                    Text(
                        text = "와블이 추천드려요!",
                        style = WableTheme.typography.body01,
                        color = Color(0xFF9269EA),
                    )
                    Text(
                        text = "그동안 어떤 일들이 있었을까요?",
                        style = WableTheme.typography.caption02,
                        color = Color(0xFF9469EA),
                    )
                }
            }
        }
        items(
            items = newsList,
            key = { item -> item.newsId },
        ) { item ->
            WableNewsItems(
                newsItem = item,
                onClick = navigateToProfile,
            )
        }
    }
}

val newsList = List(20) { index ->
    NewsInfoModel(
        newsId = (index + 1).toLong(),
        newsTitle = "제목입니다 제목입니다 제목입니다 제..",
        newsText = "${(index + 1)} 본문이 들어가는 aaaaaaaaaaaaaaaaaaaaaaa",
        newsImage = "aaaa",
        time = "2024-02-06 23:46:26",
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WableTheme {
        NewsNewsScreen(
            navigateToProfile = {},
        )
    }
}
