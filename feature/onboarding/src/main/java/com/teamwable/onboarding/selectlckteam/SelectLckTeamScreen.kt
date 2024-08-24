package com.teamwable.onboarding.selectlckteam

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.teamwable.designsystem.component.button.WableButton
import com.teamwable.designsystem.extension.modifier.noRippleDebounceClickable
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.designsystem.type.LckTeamType
import com.teamwable.designsystem.type.MemberInfoType
import com.teamwable.navigation.Route
import com.teamwable.onboarding.R
import com.teamwable.onboarding.selectlckteam.component.LckTeamItem
import com.teamwable.onboarding.selectlckteam.model.SelectLckTeamSideEffect

@Composable
fun SelectLckTeamRoute(
    viewModel: SelectLckTeamViewModel = hiltViewModel(),
    args: Route.SelectLckTeam,
    navigateToProfile: (List<String>) -> Unit,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    var userMutableList by remember { mutableStateOf(args.userList) }

    LaunchedEffect(lifecycleOwner) {
        viewModel.firstLckWatchSideEffect.flowWithLifecycle(lifecycleOwner.lifecycle)
            .collect { sideEffect ->
                when (sideEffect) {
                    is SelectLckTeamSideEffect.NavigateToProfile -> navigateToProfile(userMutableList)
                    else -> Unit
                }
            }
    }

    SelectLckTeamScreen(
        onNextBtnClick = {
            userMutableList = userMutableList.toMutableList().apply {
                set(MemberInfoType.MEMBER_FAN_TEAM.ordinal, it)
            }
            viewModel.navigateToProfile()
        },
    )
}

@Composable
fun SelectLckTeamScreen(
    onNextBtnClick: (String) -> Unit,
) {
    var selectedTeamIndex by remember { mutableIntStateOf(-1) } // 선택된 팀 인덱스 상태
    val shuffledTeams = remember { LckTeamType.entries.shuffled() }
    // 선택된 팀의 이름을 미리 준비해둠
    val selectedTeamName: String = if (selectedTeamIndex != -1) {
        stringResource(id = shuffledTeams[selectedTeamIndex].teamName)
    } else {
        ""
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
                itemsIndexed(shuffledTeams) { index, team ->
                    val isSelected = index == selectedTeamIndex
                    LckTeamItem(
                        lckTeamType = team,
                        enabled = isSelected, // 선택 상태에 따라 색상 변경
                        onClick = {
                            selectedTeamIndex = if (isSelected) -1 else index // 클릭 시 선택 토글
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
                if (selectedTeamIndex != -1) onNextBtnClick(selectedTeamName)
            },
            enabled = selectedTeamIndex != -1,
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
