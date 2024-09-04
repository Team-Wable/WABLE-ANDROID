package com.teamwable.onboarding.agreeterms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.teamwable.designsystem.component.button.WableButton
import com.teamwable.designsystem.component.checkbox.WableCheckBoxWithText
import com.teamwable.designsystem.component.dialog.WableButtonDialog
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.designsystem.type.DialogType
import com.teamwable.navigation.Route
import com.teamwable.onboarding.R
import com.teamwable.onboarding.agreeterms.model.AgreeTerm
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
    val showDialog by viewModel.showDialog.collectAsStateWithLifecycle()

    Timber.d("args: ${args.userList}")

    LaunchedEffect(lifecycleOwner) {
        viewModel.sideEffect.flowWithLifecycle(lifecycleOwner.lifecycle)
            .collect { sideEffect ->
                when (sideEffect) {
                    is AgreeTermsSideEffect.NavigateToHome -> navigateToHome()
                    is AgreeTermsSideEffect.ShowDialog -> viewModel.showLoginDialog(true)
                    is AgreeTermsSideEffect.ShowSnackBar -> onShowErrorSnackBar(sideEffect.message)
                }
            }
    }

    AgreeTermsScreen(
        onNextBtnClick = { marketingConsent ->
            viewModel.showLoginDialog(true)
            Timber.tag("consent").d(marketingConsent.toString())
        },
    )

    if (showDialog) {
        WableButtonDialog(
            dialogType = DialogType.WELLCOME,
            onClick = { viewModel.navigateToHome() },
            onDismissRequest = { viewModel.showLoginDialog(false) },
        )
    }
}

@Composable
fun AgreeTermsScreen(
    onNextBtnClick: (Boolean) -> Unit,
) {
    var allChecked by remember { mutableStateOf(false) } // 전체 선택
    var checkedStates by remember { mutableStateOf(AgreeTerm.entries.map { false }) } // 개별 선택

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
                text = stringResource(R.string.agree_terms_title),
                style = WableTheme.typography.head00,
                color = WableTheme.colors.black,
                modifier = Modifier.padding(top = 16.dp),
            )

            WableCheckBoxWithText(
                checked = allChecked,
                textStyle = WableTheme.typography.body01,
                text = "전체 선택",
                onCheckedChange = { isChecked ->
                    allChecked = isChecked
                    checkedStates = AgreeTerm.entries.map { isChecked }
                },
                modifier = Modifier.padding(top = 28.dp),
            )

            HorizontalDivider(
                thickness = 1.dp,
                color = WableTheme.colors.gray300,
                modifier = Modifier.padding(
                    vertical = 16.dp,
                ),
            )

            AgreeTerm.entries.forEachIndexed { index, item ->
                WableCheckBoxWithText(
                    checked = checkedStates[index],
                    textStyle = WableTheme.typography.body02,
                    text = stringResource(id = item.label),
                    onCheckedChange = { isChecked ->
                        val newCheckedStates = checkedStates.toMutableList()
                        newCheckedStates[index] = isChecked
                        checkedStates = newCheckedStates
                        allChecked = newCheckedStates.all { it }
                    },
                    modifier = Modifier.padding(bottom = 4.dp),
                )
            }
        }

        WableButton(
            text = stringResource(R.string.btn_next_text),
            onClick = {
                onNextBtnClick(checkedStates[AgreeTerm.MARKETING_CONSENT.ordinal])
            },
            enabled = checkedStates.take(3).all { it },
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
