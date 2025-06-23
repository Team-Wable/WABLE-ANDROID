package com.teamwable.viewit.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.teamwable.designsystem.component.button.FloatingButtonDefaults
import com.teamwable.designsystem.component.button.WableFloatingButton
import com.teamwable.designsystem.component.layout.WableFloatingButtonLayout
import com.teamwable.designsystem.component.paging.WablePagingSpinner
import com.teamwable.designsystem.component.screen.NewsNoticeEmptyScreen
import com.teamwable.designsystem.component.screen.WablePagingScreen
import com.teamwable.designsystem.extension.composable.scrollToTop
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.designsystem.type.ContentType
import com.teamwable.model.viewit.ViewIt
import com.teamwable.ui.type.BottomSheetType
import com.teamwable.ui.type.SnackbarType
import com.teamwable.viewit.R
import com.teamwable.viewit.component.ViewitItem
import com.teamwable.viewit.viewit.ViewItViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.timeout
import kotlin.time.Duration.Companion.seconds

@Composable
fun ViewItRoute(
    viewModel: ViewItViewModel = hiltViewModel(),
    onShowBottomSheet: (BottomSheetType) -> Unit = {},
    onDismissBottomSheet: () -> Unit = {},
    onNavigateToError: () -> Unit = {},
    onNavigateToMemberProfile: (Long) -> Unit = {},
    onNavigateToMyProfile: () -> Unit = {},
    onShowSnackBar: (SnackbarType, Throwable?) -> Unit = { _, _ -> },
    onOpenUrl: (String) -> Unit = {},
    onNavigateToPosting: () -> Unit = {},
) {
    val viewIts = viewModel.viewItPagingFlow.collectAsLazyPagingItems()
    val actions = rememberViewItActions(viewModel)
    val lifecycleOwner = LocalLifecycleOwner.current
    val listState = rememberLazyListState()

    LaunchedEffect(lifecycleOwner) {
        viewModel.sideEffect.flowWithLifecycle(lifecycleOwner.lifecycle)
            .collectLatest { sideEffect ->
                when (sideEffect) {
                    is ViewItSideEffect.Navigation.ToMemberProfile -> onNavigateToMemberProfile(sideEffect.id)
                    is ViewItSideEffect.Navigation.ToUrl -> onOpenUrl(sideEffect.url)
                    ViewItSideEffect.Navigation.ToMyProfile -> onNavigateToMyProfile()
                    ViewItSideEffect.Navigation.ToError -> onNavigateToError()
                    ViewItSideEffect.Navigation.ToPosting -> onNavigateToPosting()

                    is ViewItSideEffect.UI.ShowSnackBar -> onShowSnackBar(sideEffect.type, sideEffect.throwable)
                    is ViewItSideEffect.UI.ShowBottomSheet -> onShowBottomSheet(sideEffect.type)
                    ViewItSideEffect.UI.DismissBottomSheet -> onDismissBottomSheet()
                    ViewItSideEffect.UI.Refresh -> {
                        viewIts.refresh()
                        awaitRefreshComplete(viewIts)
                        listState.scrollToTop()
                    }
                }
            }
    }

    ViewItScreen(actions, viewIts, listState)
}

@Composable
fun rememberViewItActions(
    viewModel: ViewItViewModel = hiltViewModel(),
): ViewItActions {
    return remember(viewModel) {
        ViewItActions(
            onClickProfile = { viewModel.onIntent(ViewItIntent.ClickProfile(it)) },
            onClickKebab = { viewModel.onIntent(ViewItIntent.ClickKebabBtn(it)) },
            onClickLink = { viewModel.onIntent(ViewItIntent.ClickLink(it)) },
            onClickLike = { viewModel.onIntent(ViewItIntent.ClickLikeBtn(it)) },
            onClickPosting = { viewModel.onIntent(ViewItIntent.ClickPosting) },
            onRefresh = { viewModel.onIntent(ViewItIntent.PullToRefresh) },
        )
    }
}

suspend fun awaitRefreshComplete(data: LazyPagingItems<*>) = runCatching {
    snapshotFlow { data.loadState.refresh }
        .map { it is LoadState.NotLoading }
        .distinctUntilChanged()
        .filter { it }
        .timeout(5.seconds)
        .first()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewItScreen(
    actions: ViewItActions,
    viewIts: LazyPagingItems<ViewIt>,
    listState: LazyListState,
) {
    WableFloatingButtonLayout(
        buttonContent = { modifier ->
            WableFloatingButton(
                modifier = modifier.padding(20.dp),
                onClick = actions.onClickPosting,
                icon = painterResource(id = com.teamwable.common.R.drawable.ic_home_posting),
                contentDescription = "추가",
                style = FloatingButtonDefaults.gradientStyle(),
            )
        },
        buttonAlignment = Alignment.BottomEnd,
    ) {
        WablePagingScreen(
            lazyPagingItems = viewIts,
            onRefresh = actions.onRefresh,
            emptyContent = { NewsNoticeEmptyScreen(emptyTxt = R.string.label_view_it_empty) },
            content = { ViewItListContent(viewIts = viewIts, listState = listState, actions = actions) },
        )
    }
}

@Composable
fun ViewItListContent(
    viewIts: LazyPagingItems<ViewIt>,
    listState: LazyListState,
    actions: ViewItActions,
) {
    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(bottom = 108.dp),
        modifier = Modifier
            .fillMaxSize()
            .testTag("viewit_list"),
    ) {
        items(
            count = viewIts.itemCount,
            key = viewIts.itemKey { it.viewItId },
        ) { index ->
            ViewitItem(
                viewIts[index] ?: return@items,
                actions,
            )
            HorizontalDivider(
                thickness = 1.dp,
                color = WableTheme.colors.gray200,
            )
        }

        if (viewIts.loadState.append is LoadState.Loading) {
            item(
                key = ContentType.Spinner.name,
                contentType = viewIts.itemContentType { ContentType.Spinner.name },
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
