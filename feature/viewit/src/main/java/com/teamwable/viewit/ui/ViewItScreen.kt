package com.teamwable.viewit.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.teamwable.designsystem.component.button.FloatingButtonDefaults
import com.teamwable.designsystem.component.button.WableFloatingButton
import com.teamwable.designsystem.component.layout.WableFloatingButtonLayout
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.model.viewit.ViewIt
import com.teamwable.viewit.component.ViewitItem
import com.teamwable.viewit.viewit.ViewItViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf

@Composable
fun ViewItRoute(
    viewModel: ViewItViewModel = hiltViewModel(),
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val viewIts = viewModel.viewItPagingFlow.collectAsLazyPagingItems()

    LaunchedEffect(lifecycleOwner) {
        viewModel.sideEffect.flowWithLifecycle(lifecycleOwner.lifecycle)
            .collectLatest { sideEffect ->
                when (sideEffect) {
                    ViewItSideEffect.NavigateToProfile -> TODO()
                    is ViewItSideEffect.ShowSnackBar -> TODO()
                }
            }
    }

    ViewItScreen(
        uiState = uiState,
        viewIts,
        onClickProfile = {},
        onClickKebab = {},
        onClickLink = {},
        onClickLike = {},
    )
}

@Composable
private fun ViewItScreen(
    uiState: ViewItUiState,
    viewIts: LazyPagingItems<ViewIt>,
    onClickProfile: (ViewIt) -> Unit = {},
    onClickKebab: (ViewIt) -> Unit = {},
    onClickLink: (ViewIt) -> Unit = {},
    onClickLike: (ViewIt) -> Unit = {},
) {
    WableFloatingButtonLayout(
        buttonContent = { modifier ->
            WableFloatingButton(
                modifier = modifier.padding(20.dp),
                onClick = { },
                icon = painterResource(id = com.teamwable.common.R.drawable.ic_home_posting),
                contentDescription = "추가",
                style = FloatingButtonDefaults.gradientStyle(),
            )
        },
        buttonAlignment = Alignment.BottomEnd,
    ) {
        LazyColumn {
            items(
                count = viewIts.itemCount,
                key = viewIts.itemKey { it.viewItId },
            ) { index ->
                ViewitItem(
                    viewIts[index] ?: return@items,
                    onClickProfile,
                    onClickKebab,
                    onClickLink,
                    onClickLike,
                )
                HorizontalDivider(
                    thickness = 1.dp,
                    color = WableTheme.colors.gray200,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ViewItScreenPreview() {
    WableTheme {
        ViewItScreen(
            uiState = ViewItUiState(),
            viewIts = flowOf(PagingData.from(emptyList<ViewIt>())).collectAsLazyPagingItems(),
        )
    }
}
