package com.teamwable.onboarding.profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.navigation.Route
import com.teamwable.onboarding.selectlckteam.SelectLckTeamScreen
import com.teamwable.onboarding.selectlckteam.SelectLckTeamViewModel
import timber.log.Timber

@Composable
fun ProfileRoute(
    viewModel: SelectLckTeamViewModel = hiltViewModel(),
    args: Route.Profile,
    navigateToProfile: (List<String>) -> Unit,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val userMutableList = args.userList.toMutableList()

    Timber.d("ProfileRoute: $userMutableList")
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
