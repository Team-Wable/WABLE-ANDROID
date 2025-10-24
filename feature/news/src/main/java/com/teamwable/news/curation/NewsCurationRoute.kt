package com.teamwable.news.curation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.teamwable.designsystem.component.paging.WablePagingSpinner
import com.teamwable.designsystem.type.ContentType
import com.teamwable.model.news.CurationModel
import com.teamwable.news.notice.NewsNoticeViewModel

@Composable
fun NewsCurationRoute(
    viewModel: NewsNoticeViewModel = hiltViewModel(),
    onOpenUrl: (String) -> Unit = {},
) {
    val lifecycleOwner = LocalLifecycleOwner.current
}

@Composable
fun CurationScreen(
    curations: LazyPagingItems<CurationModel>,
    listState: LazyListState,
    onItemClick: (String) -> Unit,
) {
    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(bottom = 108.dp),
        modifier = Modifier
            .fillMaxSize()
            .testTag("curation_list"),
    ) {
        items(
            count = curations.itemCount,
            key = curations.itemKey { it.curationId },
        ) { index ->
            NewsCurationItem(
                curations[index] ?: return@items,
                onItemClick,
            )
        }

        if (curations.loadState.append is LoadState.Loading) {
            item(
                key = ContentType.Spinner.name,
                contentType = curations.itemContentType { ContentType.Spinner.name },
            ) {
                WablePagingSpinner(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                )
            }
        }
    }
}
