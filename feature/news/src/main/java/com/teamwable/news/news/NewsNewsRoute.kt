package com.teamwable.news.news

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.teamwable.designsystem.component.paging.WablePagingSpinner
import com.teamwable.designsystem.component.screen.LoadingScreen
import com.teamwable.designsystem.component.screen.NewsNoticeEmptyScreen
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.model.news.NewsInfoModel
import com.teamwable.news.R
import com.teamwable.news.news.component.WableNewsItems
import com.teamwable.news.news.component.WableNewsTopBanner
import com.teamwable.news.news.model.NewsInfoSideEffect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf

@Composable
fun NewsNewsRoute(
    viewModel: NewsNewsViewModel = hiltViewModel(),
    navigateToDetail: (NewsInfoModel) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val newsItems = viewModel.newsPagingFlow.collectAsLazyPagingItems()

    LaunchedEffect(lifecycleOwner) {
        viewModel.sideEffect.flowWithLifecycle(lifecycleOwner.lifecycle)
            .collectLatest { sideEffect ->
                when (sideEffect) {
                    is NewsInfoSideEffect.NavigateToDetail -> navigateToDetail(sideEffect.newsInfoModel)
                    else -> Unit
                }
            }
    }

    NewsNewsScreen(
        newsItems = newsItems,
        onItemClick = viewModel::onItemClick,
    )
}

@Composable
fun NewsNewsScreen(
    newsItems: LazyPagingItems<NewsInfoModel>,
    onItemClick: (NewsInfoModel) -> Unit,
) {
    val isLoading = newsItems.loadState.refresh is LoadState.Loading
    val isEmpty = newsItems.itemCount == 0 && !isLoading
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 4.dp),
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

        when {
            isLoading -> item { LoadingScreen() }
            isEmpty -> item { NewsNoticeEmptyScreen(emptyTxt = R.string.tv_news_info_empty) }
            else -> {
                items(
                    count = newsItems.itemCount,
                    key = newsItems.itemKey { it },
                    contentType = newsItems.itemContentType { "MyPagingItems" },
                ) { index ->
                    newsItems[index]?.let {
                        WableNewsItems(
                            newsItem = it,
                            onClick = onItemClick,
                        )
                    }
                }
                item {
                    if (newsItems.loadState.append is LoadState.Loading) {
                        WablePagingSpinner(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WableTheme {
        NewsNewsScreen(
            onItemClick = {},
            newsItems = flowOf(PagingData.from(emptyList<NewsInfoModel>())).collectAsLazyPagingItems(),
        )
    }
}
