package com.teamwable.main_compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.teamwable.auth.naviagation.loginNavGraph
import com.teamwable.common.intentprovider.IntentProvider
import com.teamwable.common.util.getThrowableMessage
import com.teamwable.designsystem.component.snackbar.SNACK_BAR_DURATION
import com.teamwable.designsystem.component.snackbar.WableSnackBar
import com.teamwable.designsystem.component.topbar.WableAppBar
import com.teamwable.designsystem.type.SnackBarType
import com.teamwable.main_compose.splash.navigation.splashNavGraph
import com.teamwable.onboarding.agreeterms.naviagation.agreeTermsNavGraph
import com.teamwable.onboarding.firstlckwatch.naviagation.firstLckWatchNavGraph
import com.teamwable.onboarding.profile.naviagation.profileNavGraph
import com.teamwable.onboarding.selectlckteam.naviagation.selectLckTeamNavGraph
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
internal fun MainScreen(
    navigator: MainNavigator = rememberMainNavigator(),
    intentProvider: IntentProvider,
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val localContext = LocalContext.current
    val intent = intentProvider.getIntent()

    val coroutineScope = rememberCoroutineScope()
    val onShowErrorSnackBar: (throwable: Throwable?) -> Unit = { throwable ->
        coroutineScope.launch {
            val job = launch {
                snackBarHostState.showSnackbar(message = throwable.getThrowableMessage())
            }
            delay(SNACK_BAR_DURATION)
            job.cancel()
        }
    }

    Scaffold(
        topBar = {
            WableAppBar(
                modifier = Modifier.statusBarsPadding(),
                visibility = navigator.shouldShowTopBar(),
                canNavigateBack = navigator.isBackStackNotEmpty(),
                navigateUp = { navigator.navigateUp() },
                resetToLogin = { navigator.resetToLogin() },
            )
        },
        content = { innerPadding ->
            Box(
                modifier =
                    if (!navigator.shouldShowTopBar()) Modifier.fillMaxSize()
                    else Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
            ) {
                NavHost(
                    navController = navigator.navController,
                    startDestination = navigator.startDestination,
                ) {
                    splashNavGraph(
                        navigateToLogIn = {
                            val navOptions = navOptions {
                                popUpTo(navigator.navController.graph.findStartDestination().id) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                            navigator.navigateToLogin(
                                navOptions = navOptions,
                            )
                        },
                        navigateToHome = { localContext.startActivity(intent) },
                    )
                    loginNavGraph(
                        navigateToFirstLckWatch = { navigator.navigateToFirstLckWatch() },
                        navigateToHome = { localContext.startActivity(intent) },
                        onShowErrorSnackBar = onShowErrorSnackBar,
                    )
                    firstLckWatchNavGraph(
                        navigateToSelectLckTeam = navigator::navigateToSelectLckTeam,
                        onShowErrorSnackBar = onShowErrorSnackBar,
                    )
                    selectLckTeamNavGraph(
                        navigateToProfile = navigator::navigateToProfile,
                        onShowErrorSnackBar = onShowErrorSnackBar,
                    )
                    profileNavGraph(
                        navigateToAgreeTerms = navigator::navigateToAgreeTerms,
                        onShowErrorSnackBar = onShowErrorSnackBar,
                    )
                    agreeTermsNavGraph(
                        navigateToHome = { localContext.startActivity(intent) },
                        onShowErrorSnackBar = onShowErrorSnackBar,
                    )
                }
            }
            SnackbarHost(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp)
                    .zIndex(1f),
                hostState = snackBarHostState,
                snackbar = { snackBarData ->
                    WableSnackBar(
                        message = snackBarData.visuals.message,
                        snackBarType = SnackBarType.ERROR,
                    )
                },
            )
        },
    )
}
