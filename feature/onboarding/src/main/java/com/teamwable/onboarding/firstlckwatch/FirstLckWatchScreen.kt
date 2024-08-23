package com.teamwable.onboarding.firstlckwatch

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import com.teamwable.designsystem.component.button.WableButton
import com.teamwable.designsystem.extension.system.SetStatusBarColor
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.onboarding.R
import com.teamwable.onboarding.firstlckwatch.component.WableExposedDropdownBox
import com.teamwable.onboarding.firstlckwatch.model.FirstLckWatchSideEffect
import kotlinx.collections.immutable.toPersistentList

@Composable
fun FirstLckWatchRoute(
    viewModel: FirstLckWatchViewModel = hiltViewModel(),
    navigateToSelectLckTeam: () -> Unit,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(lifecycleOwner) {
        viewModel.firstLckWatchSideEffect.flowWithLifecycle(lifecycleOwner.lifecycle)
            .collect { sideEffect ->
                when (sideEffect) {
                    is FirstLckWatchSideEffect.NavigateToSelectLckTeam -> navigateToSelectLckTeam()
                    else -> Unit
                }
            }
    }

    FirstLckWatchScreen(
        onNextBtnClick = { viewModel.navigateToSelectTeam() },
    )
}

@Composable
fun FirstLckWatchScreen(
    onNextBtnClick: (String) -> Unit,
) {
    SetStatusBarColor(color = WableTheme.colors.white)

    val options = (2012..2024).map { it.toString() }.toPersistentList()

    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by rememberSaveable { mutableIntStateOf(options.lastIndex) }
    val listState = rememberLazyListState()

    // 드롭다운이 열릴 때 선택된 항목으로 스크롤
    LaunchedEffect(expanded) {
        if (expanded) {
            listState.scrollToItem(selectedIndex)
        }
    }

    Column(
        verticalArrangement = Arrangement.SpaceBetween, // 상단과 하단을 공간으로 분리
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(id = com.teamwable.common.R.dimen.padding_horizontal)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            Text(
                text = stringResource(R.string.first_lck_watch_title),
                style = WableTheme.typography.head00,
                color = WableTheme.colors.black,
                modifier = Modifier.padding(top = 16.dp),
            )

            Text(
                text = stringResource(R.string.first_lck_watch_description),
                style = WableTheme.typography.body02,
                color = WableTheme.colors.gray600,
                modifier = Modifier.padding(top = 6.dp),
            )

            Text(
                text = stringResource(R.string.first_lck_watch_year_text),
                style = WableTheme.typography.caption03,
                color = WableTheme.colors.purple50,
                modifier = Modifier.padding(top = 34.dp),
            )

            WableExposedDropdownBox(
                expanded = expanded,
                options = options,
                listState = listState,
                onExpandedChange = { expanded = it },
                selectedIndex = selectedIndex,
                onSelectedIndexChange = { selectedIndex = it },
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
                    .weight(1f),
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        WableButton(
            text = stringResource(R.string.btn_next_text),
            onClick = { onNextBtnClick(options[selectedIndex]) },
            enabled = true,
            textStyle = WableTheme.typography.body01,
            modifier = Modifier.padding(bottom = 24.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WableTheme {
        FirstLckWatchScreen(
            onNextBtnClick = {},
        )
    }
}
