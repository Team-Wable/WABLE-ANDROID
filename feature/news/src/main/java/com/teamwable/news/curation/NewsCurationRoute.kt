package com.teamwable.news.curation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.teamwable.designsystem.component.paging.WablePagingSpinner
import com.teamwable.designsystem.component.screen.WablePagingScreen
import com.teamwable.designsystem.type.ContentType
import com.teamwable.model.news.CurationModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsCurationRoute(
    viewModel: NewsCurationViewModel = hiltViewModel(),
    onOpenUrl: (String) -> Unit = {},
    onShowSnackBar: (Throwable?) -> Unit = {},
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val curations = viewModel.curationPagingFlow.collectAsLazyPagingItems()
    val listState = rememberLazyListState()

    LaunchedEffect(lifecycleOwner) {
        viewModel.sideEffect.flowWithLifecycle(lifecycleOwner.lifecycle)
            .collectLatest { sideEffect ->
                when (sideEffect) {
                    is NewsCurationSideEffect.Navigation.ToUrl -> onOpenUrl(sideEffect.url)
                    is NewsCurationSideEffect.UI.ShowSnackBar -> onShowSnackBar(sideEffect.throwable)
                    NewsCurationSideEffect.UI.Refresh -> curations.refresh()
                }
            }
    }

    WablePagingScreen(
        lazyPagingItems = curations,
        onRefresh = { viewModel.onIntent(NewsCurationIntent.PullToRefresh) },
        content = {
            CurationScreen(
                curations,
                listState,
                onItemClick = { viewModel.onIntent(NewsCurationIntent.ClickLink(it)) },
            )
        },
    )
}

@Composable
fun CurationScreen(
    curations: LazyPagingItems<CurationModel>,
    listState: LazyListState,
    onItemClick: (String) -> Unit,
) {
    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(top = 4.dp, bottom = 10.dp),
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
                contentType = ContentType.Spinner.name,
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
