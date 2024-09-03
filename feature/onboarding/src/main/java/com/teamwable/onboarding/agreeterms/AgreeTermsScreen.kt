package com.teamwable.onboarding.agreeterms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import com.teamwable.designsystem.component.button.WableButton
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.navigation.Route
import com.teamwable.onboarding.R
import com.teamwable.onboarding.agreeterms.model.AgreeTermsSideEffect
import timber.log.Timber

@Composable
fun AgreeTermsRoute(
    viewModel: AgreeTermsViewModel = hiltViewModel(),
    args: Route.AgreeTerms,
    navigateToHome: () -> Unit,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    Timber.d("args: ${args.userList}")

    LaunchedEffect(lifecycleOwner) {
        viewModel.sideEffect.flowWithLifecycle(lifecycleOwner.lifecycle)
            .collect { sideEffect ->
                when (sideEffect) {
                    is AgreeTermsSideEffect.NavigateToHome -> navigateToHome()
                    else -> Unit
                }
            }
    }

    AgreeTermsScreen(
        onNextBtnClick = {},
    )
}

@Composable
fun AgreeTermsScreen(
    onNextBtnClick: (String) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
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
                text = "와블 이용을 위해\n동의가 필요해요",
                style = WableTheme.typography.head00,
                color = WableTheme.colors.black,
                modifier = Modifier.padding(top = 16.dp),
            )
        }

        WableButton(
            text = stringResource(R.string.btn_next_text),
            onClick = {},
            enabled = false,
            modifier = Modifier.padding(bottom = 24.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WableTheme {
        AgreeTermsScreen(
            onNextBtnClick = {},
        )
    }
}
