package com.teamwable.onboarding.selectlckteam

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.teamwable.designsystem.component.button.WableButton
import com.teamwable.designsystem.extension.system.SetStatusBarColor
import com.teamwable.designsystem.theme.WableTheme

@Composable
fun SelectLckTeamRoute(
    viewModel: SelectLckTeamViewModel = hiltViewModel(),
    navigateToSelectLckTeam: () -> Unit,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    /*    LaunchedEffect(lifecycleOwner) {
            viewModel.firstLckWatchSideEffect.flowWithLifecycle(lifecycleOwner.lifecycle)
                .collect { sideEffect ->
                    when (sideEffect) {
                        is FirstLckWatchSideEffect.NavigateToSelectLckTeam -> navigateToSelectLckTeam()
                        else -> Unit
                    }
                }
        }*/

    SelectLckTeamScreen(
        onNextBtnClick = { },
    )
}

@Composable
fun SelectLckTeamScreen(
    onNextBtnClick: () -> Unit,
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
        modifier = Modifier.fillMaxSize(),
    ) {
        Text(text = "SelectLckTeamScreen")

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(top = 18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
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
                .padding(bottom = 12.dp)
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
            text = "다음으로",
            onClick = onNextBtnClick,
            enabled = true,
            textStyle = WableTheme.typography.body01,
        )
        Spacer(modifier = Modifier.weight(1f))
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
