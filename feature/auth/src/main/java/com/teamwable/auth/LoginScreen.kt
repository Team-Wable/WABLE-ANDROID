package com.teamwable.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.teamwable.common.intentprovider.IntentProvider
import com.teamwable.designsystem.theme.WableTheme

@Composable
fun LoginRoute(
    navigateToOnBoarding: () -> Unit,
    navigateToHome: () -> Unit,
    onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
    intentProvider: IntentProvider,
) {
    val context = LocalContext.current

    LoginScreen(
        onLoginBtnClick = {
            val intent = intentProvider.getIntent()
            startActivity(context, intent, null)
        },
    )
}

@Composable
fun LoginScreen(
    onLoginBtnClick: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Button(
            shape = RoundedCornerShape(8.dp),
//            colors = ButtonDefaults.buttonColors(containerColor = Yellow),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 20.dp),
            onClick = { onLoginBtnClick() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Image(
                    painter = painterResource(id = com.teamwable.common.R.drawable.ic_login_kakao),
                    contentDescription = "kakao",
                    modifier = Modifier.align(Alignment.CenterStart),
                )
                Text(
                    text = stringResource(R.string.login_kakao_btn_text),
//                    style = KkumulTheme.typography.body03,
//                    color = Gray8,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center),
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WableTheme {
        LoginScreen(
            onLoginBtnClick = {},
        )
    }
}
