package com.teamwable.community

import android.content.ClipData
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.teamwable.common.util.LinkStorage
import com.teamwable.community.component.CommunityButtonType
import com.teamwable.community.component.CommunityHeader
import com.teamwable.community.component.CommunityItem
import com.teamwable.community.component.HandleDialog
import com.teamwable.community.model.CommunityIntent
import com.teamwable.community.model.CommunitySideEffect
import com.teamwable.community.model.CommunityState
import com.teamwable.designsystem.component.button.BoardRequestButton
import com.teamwable.designsystem.component.layout.WableFloatingButtonLayout
import com.teamwable.designsystem.extension.composable.scrollToTop
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.designsystem.type.ContentType
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CommunityRoute(
    viewModel: CommunityViewModel = hiltViewModel(),
    navigateToGoogleForm: () -> Unit = {},
    navigateToPushAlarm: () -> Unit = {},
    onShowErrorSnackBar: (Throwable) -> Unit = {},
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    val clipboardManager = LocalClipboardManager.current
    val listState = rememberLazyListState()

    LaunchedEffect(lifecycleOwner) {
        viewModel.sideEffect.flowWithLifecycle(lifecycleOwner.lifecycle)
            .collectLatest { sideEffect ->
                when (sideEffect) {
                    CommunitySideEffect.CopyToClipBoard -> {
                        val clipData = ClipData.newPlainText("pre link", LinkStorage.PRE_REGISTER_LINK)
                        clipboardManager.setClip(ClipEntry(clipData))
                    }

                    CommunitySideEffect.NavigateToGoogleForm -> navigateToGoogleForm()
                    CommunitySideEffect.NavigateToPushAlarm -> navigateToPushAlarm()
                    is CommunitySideEffect.ShowSnackBar -> onShowErrorSnackBar(sideEffect.throwable)
                    CommunitySideEffect.ScrollToTop -> listState.scrollToTop()
                }
            }
    }

    HandleDialog(
        state = state,
        onDismissRequest = { viewModel.onIntent(CommunityIntent.ClickDismissBtn) },
        onPreRegisterBtnClick = { viewModel.onIntent(CommunityIntent.ClickPreRegisterBtn) },
        onPreRegisterCancelBtnClick = { viewModel.onIntent(CommunityIntent.ClickPreRegisterDismissBtn) },
        onPushBtnClick = { viewModel.onIntent(CommunityIntent.ClickPushBtn) },
        onIntent = viewModel::onIntent,
    )

    CommunityScreen(
        state = state,
        listState = listState,
        onDefaultBtnClick = { team -> viewModel.onIntent(CommunityIntent.ClickDefaultItemBtn(team)) },
        onMoreFanBtnClick = { viewModel.onIntent(CommunityIntent.ClickMoreFanItemBtn) },
        onFloatingBtnClick = { viewModel.onIntent(CommunityIntent.ClickFloatingBtn) },
    )
}

@Composable
private fun CommunityScreen(
    state: CommunityState,
    listState: LazyListState,
    onDefaultBtnClick: (String) -> Unit = {},
    onFloatingBtnClick: () -> Unit = {},
    onMoreFanBtnClick: () -> Unit = {},
) {
    WableFloatingButtonLayout(
        buttonContent = { modifier ->
            BoardRequestButton(
                modifier = modifier,
                onClick = onFloatingBtnClick,
            )
        },
    ) {
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(top = 10.dp, bottom = 64.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            item(
                contentType = ContentType.Banner.name,
            ) {
                CommunityHeader(
                    modifier = Modifier.padding(
                        horizontal = dimensionResource(com.teamwable.common.R.dimen.padding_horizontal),
                    ),
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            items(
                items = state.lckTeams,
                key = { items -> items.communityName },
                contentType = { ContentType.Item.name },
            ) { item ->
                val isSelected = item.communityName == state.preRegisterTeamName
                CommunityItem(
                    lckTeamType = item,
                    progress = state.progress,
                    type = if (isSelected) state.buttonState else CommunityButtonType.DEFAULT,
                    enabled = state.preRegisterTeamName.isBlank() || isSelected,
                    onClick = {
                        when (state.buttonState) {
                            CommunityButtonType.DEFAULT -> onDefaultBtnClick(item.communityName)
                            CommunityButtonType.FAN_MORE -> onMoreFanBtnClick()
                            else -> Unit
                        }
                    },
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CommunityPreview() {
    WableTheme {
        CommunityScreen(
            state = CommunityState(),
            listState = rememberLazyListState(),
        )
    }
}
