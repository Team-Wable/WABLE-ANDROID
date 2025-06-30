package com.teamwable.designsystem.component.screen

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.teamwable.designsystem.component.paging.WableCustomRefreshIndicator

/**
 * Paging 데이터의 로딩 상태에 따라 적절한 UI를 표시하는 컴포넌트입니다.
 *
 * 이 컴포넌트는 [LazyPagingItems]의 상태를 자동으로 감지하여 다음 상황별로 다른 UI를 보여줍니다:
 * - 로딩 중: [loadingContent] 표시
 * - 에러 발생: [errorContent] 표시
 * - 데이터 없음: [emptyContent] 표시
 * - 정상 데이터: [content] 표시
 *
 * @param T Paging 데이터의 타입
 * @param lazyPagingItems Paging 라이브러리의 [LazyPagingItems] 객체
 * @param loadingContent 초기 로딩 상태에서 표시할 UI. 기본값은 [LoadingScreen]
 * @param emptyContent 데이터가 비어있을 때 표시할 UI.
 * @param errorContent 에러 발생 시 표시할 UI. 에러 객체를 매개변수로 받습니다.
 * @param content 정상적으로 데이터가 있을 때 표시할 UI (주로 LazyColumn/LazyRow)
 */
@Composable
fun <T : Any> PagingStateHandler(
    lazyPagingItems: LazyPagingItems<T>,
    loadingContent: @Composable () -> Unit = { LoadingScreen() },
    emptyContent: @Composable () -> Unit = {},
    errorContent: @Composable (Throwable) -> Unit = {},
    content: @Composable () -> Unit,
) {
    when {
        lazyPagingItems.loadState.refresh is LoadState.Loading -> loadingContent()
        lazyPagingItems.loadState.refresh is LoadState.Error -> {
            val error = (lazyPagingItems.loadState.refresh as LoadState.Error).error
            errorContent(error)
        }

        lazyPagingItems.itemCount == 0 && lazyPagingItems.loadState.refresh is LoadState.NotLoading -> emptyContent()

        else -> content()
    }
}

/**
 * Paging 화면 컴포넌트입니다.
 *
 * 이 컴포넌트는 Paging 데이터를 표시하는 화면에서 공통적으로 필요한 기능들을 제공합니다:
 * - Pull-to-refresh 기능
 * - 로딩/에러/빈 상태 처리 ([PagingStateHandler] 내장)
 * - Wable 디자인 시스템 기본 UI 적용
 *
 * @param T Paging 데이터의 타입
 * @param lazyPagingItems Paging 라이브러리의 [LazyPagingItems] 객체
 * @param onRefresh Pull-to-refresh 시 호출될 콜백 함수
 * @param modifier 컴포넌트에 적용할 [Modifier]
 * @param pullToRefreshEnabled Pull-to-refresh 기능 활성화 여부. 기본값은 true
 * @param refreshIndicator 커스텀 새로고침 인디케이터. 기본값은 [WablePagingDefaults.RefreshIndicator]
 * @param loadingContent 초기 로딩 상태에서 표시할 UI
 * @param emptyContent 데이터가 비어있을 때 표시할 UI
 * @param errorContent 에러 발생 시 표시할 UI
 * @param content 정상적으로 데이터가 있을 때 표시할 UI (주로 LazyColumn/LazyRow)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : Any> WablePagingScreen(
    modifier: Modifier = Modifier,
    lazyPagingItems: LazyPagingItems<T>,
    onRefresh: () -> Unit,
    pullToRefreshEnabled: Boolean = true,
    refreshIndicator: @Composable BoxScope.(PullToRefreshState, Boolean) -> Unit =
        { state, isRefreshing ->
            WableCustomRefreshIndicator(
                state = state,
                isRefreshing = isRefreshing,
                modifier = modifier.align(Alignment.TopCenter),
            )
        },
    loadingContent: @Composable () -> Unit = { LoadingScreen() },
    emptyContent: @Composable () -> Unit = { },
    errorContent: @Composable (Throwable) -> Unit = {},
    content: @Composable () -> Unit,
) {
    val refreshState = rememberPullToRefreshState()
    val isRefreshing = lazyPagingItems.itemCount != 0 &&
        lazyPagingItems.loadState.refresh is LoadState.Loading

    if (pullToRefreshEnabled) {
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            state = refreshState,
            modifier = modifier,
            indicator = { refreshIndicator(refreshState, isRefreshing) },
        ) {
            PagingStateHandler(
                lazyPagingItems = lazyPagingItems,
                loadingContent = loadingContent,
                emptyContent = emptyContent,
                errorContent = errorContent,
                content = content,
            )
        }
    } else {
        PagingStateHandler(
            lazyPagingItems = lazyPagingItems,
            loadingContent = loadingContent,
            emptyContent = emptyContent,
            errorContent = errorContent,
            content = content,
        )
    }
}
