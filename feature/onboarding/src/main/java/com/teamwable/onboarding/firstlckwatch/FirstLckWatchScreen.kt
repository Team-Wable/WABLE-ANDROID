package com.teamwable.onboarding.firstlckwatch

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import com.teamwable.designsystem.component.button.WableButton
import com.teamwable.designsystem.extension.system.SetStatusBarColor
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.onboarding.firstlckwatch.model.FirstLckWatchSideEffect

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
    onNextBtnClick: () -> Unit,
) {
    SetStatusBarColor(color = WableTheme.colors.white)

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Text(text = "FirstLckWatchScreen")

        Spacer(modifier = Modifier.weight(1f))

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
        FirstLckWatchScreen(
            onNextBtnClick = {},
        )
    }
}
