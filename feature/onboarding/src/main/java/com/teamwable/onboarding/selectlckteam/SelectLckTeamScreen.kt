package com.teamwable.onboarding.selectlckteam

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import com.teamwable.common.util.AmplitudeSignUpTag.CLICK_DETOUR_TEAM_SIGNUP
import com.teamwable.common.util.AmplitudeSignUpTag.CLICK_NEXT_TEAM_SIGNUP
import com.teamwable.common.util.AmplitudeUtil.trackEvent
import com.teamwable.designsystem.component.button.WableButton
import com.teamwable.designsystem.extension.modifier.noRippleDebounceClickable
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.designsystem.type.LckTeamType
import com.teamwable.model.profile.MemberInfoEditModel
import com.teamwable.navigation.Route
import com.teamwable.onboarding.R
import com.teamwable.onboarding.selectlckteam.component.LckTeamItem
import com.teamwable.onboarding.selectlckteam.model.SelectLckTeamSideEffect

@Composable
fun SelectLckTeamRoute(
    viewModel: SelectLckTeamViewModel = hiltViewModel(),
    args: Route.SelectLckTeam,
    navigateToProfile: (MemberInfoEditModel) -> Unit,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    var memberInfoEditModel by remember { mutableStateOf(args.memberInfoEditModel) }

    LaunchedEffect(lifecycleOwner) {
        viewModel.firstLckWatchSideEffect.flowWithLifecycle(lifecycleOwner.lifecycle)
            .collect { sideEffect ->
                when (sideEffect) {
                    is SelectLckTeamSideEffect.NavigateToProfile -> navigateToProfile(memberInfoEditModel)
                    else -> Unit
                }
            }
    }

    SelectLckTeamScreen(
        onNextBtnClick = {
            memberInfoEditModel = memberInfoEditModel.copy(memberFanTeam = it)
            viewModel.navigateToProfile()
            if (it.isNotEmpty()) trackEvent(CLICK_NEXT_TEAM_SIGNUP)
            else trackEvent(CLICK_DETOUR_TEAM_SIGNUP)
        },
    )
}

@Composable
fun SelectLckTeamScreen(
    onNextBtnClick: (String) -> Unit,
) {
    // 선택된 팀을 팀 객체로 관리
    var selectedTeam by rememberSaveable { mutableStateOf<LckTeamType?>(null) }
    val shuffledTeams = remember { LckTeamType.entries.shuffled() }
    val gridState = rememberLazyGridState()

    // 선택된 팀의 이름을 준비
    val selectedTeamName: String = selectedTeam?.let { team ->
        stringResource(id = team.teamName)
    }.orEmpty()

    // 선택된 항목으로 스크롤
    LaunchedEffect(key1 = selectedTeam) {
        selectedTeam?.let { team ->
            val index = shuffledTeams.indexOf(team)
            if (index != -1) {
                gridState.scrollToItem(index)
            }
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
                text = stringResource(R.string.select_lck_team_title),
                style = WableTheme.typography.head00,
                color = WableTheme.colors.black,
                modifier = Modifier.padding(top = 16.dp),
            )

            Text(
                text = stringResource(R.string.select_lck_team_description),
                style = WableTheme.typography.body02,
                color = WableTheme.colors.gray600,
                modifier = Modifier.padding(top = 6.dp),
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(top = 18.dp),
            ) {
                items(
                    items = shuffledTeams,
                    key = { team -> team.teamName },
                ) { team ->
                    // 팀이 선택된 상태인지 확인
                    val isSelected = team == selectedTeam
                    LckTeamItem(
                        lckTeamType = team,
                        enabled = isSelected,
                        onClick = {
                            selectedTeam = if (isSelected) null else team // 팀을 선택하거나 선택 해제
                        },
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 18.dp, bottom = 12.dp)
                .noRippleDebounceClickable {
                    selectedTeam = null
                    onNextBtnClick("")
                },
        ) {
            Text(
                text = stringResource(R.string.select_lck_team_not_yet),
                style = WableTheme.typography.body02,
                color = WableTheme.colors.gray600,
                modifier = Modifier.padding(
                    horizontal = 15.dp,
                    vertical = 11.dp,
                ),
            )
        }

        WableButton(
            text = stringResource(R.string.btn_next_text),
            onClick = {
                if (selectedTeam != null) onNextBtnClick(selectedTeamName)
            },
            enabled = selectedTeam != null,
            modifier = Modifier.padding(bottom = 24.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WableTheme {
        SelectLckTeamScreen(
            onNextBtnClick = {},
        )
    }
}
