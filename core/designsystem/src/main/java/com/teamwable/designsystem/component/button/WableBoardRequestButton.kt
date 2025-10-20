package com.teamwable.designsystem.component.button

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.teamwable.designsystem.R
import com.teamwable.designsystem.theme.WableTheme

@Composable
fun BoardRequestButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    WableAnnotatedTextButton(
        text = getAnnotatedString(),
        onClick = onClick,
        modifier = modifier,
        buttonStyle = BigButtonDefaults.blackBigButtonStyle(),
    )
}

@Composable
private fun getAnnotatedString(): AnnotatedString {
    val annotatedText = buildAnnotatedString {
        append(stringResource(R.string.str_community_floating_main))
        withStyle(style = SpanStyle(color = WableTheme.colors.sky50)) {
            append(stringResource(R.string.str_community_floating_sub))
        }
    }
    return annotatedText
}
