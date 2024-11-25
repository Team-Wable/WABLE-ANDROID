package com.teamwable.news.notice

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
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
import com.teamwable.designsystem.type.ContentType
import com.teamwable.model.news.NewsInfoModel
import com.teamwable.news.R
import com.teamwable.news.news.model.NewsInfoSideEffect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf

@Composable
internal fun NewsNoticeRoute(
    viewModel: NewsNoticeViewModel = hiltViewModel(),
    navigateToDetail: (NewsInfoModel) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val newsItems = viewModel.noticePagingFlow.collectAsLazyPagingItems()

    LaunchedEffect(lifecycleOwner) {
        viewModel.sideEffect.flowWithLifecycle(lifecycleOwner.lifecycle)
            .collectLatest { sideEffect ->
                when (sideEffect) {
                    is NewsInfoSideEffect.NavigateToDetail -> navigateToDetail(sideEffect.newsInfoModel)
                    else -> Unit
                }
            }
    }
    NewsNoticeScreen(
        noticeItems = newsItems,
        onItemClick = viewModel::onItemClick
    )
}

@Composable
fun NewsNoticeScreen(
    noticeItems: LazyPagingItems<NewsInfoModel>,
    onItemClick: (NewsInfoModel) -> Unit
) {
    val isLoading = noticeItems.loadState.refresh is LoadState.Loading
    val isEmpty = noticeItems.itemCount == 0 && !isLoading

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        when {
            isLoading -> item { LoadingScreen() }
            isEmpty -> item { NewsNoticeEmptyScreen(emptyTxt = R.string.tv_news_notice_empty) }
            else -> {
                items(
                    count = noticeItems.itemCount,
                    key = noticeItems.itemKey { it.newsId },
                    contentType = noticeItems.itemContentType { ContentType.Item.name },
                ) { idx ->
                    noticeItems[idx]?.let {
                        NewsNoticeItem(it, onItemClick)
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = WableTheme.colors.gray200,
                        )
                    }
                }
                item(
                    key = ContentType.Spinner.name,
                    contentType = noticeItems.itemContentType { ContentType.Spinner.name },
                ) {
                    if (noticeItems.loadState.append is LoadState.Loading) {
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
        NewsNoticeScreen(
            onItemClick = {},
            noticeItems = flowOf(PagingData.from(emptyList<NewsInfoModel>())).collectAsLazyPagingItems(),
        )
    }
}
