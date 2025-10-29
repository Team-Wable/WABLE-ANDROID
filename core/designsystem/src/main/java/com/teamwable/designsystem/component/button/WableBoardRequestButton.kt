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
    textWhite: String = stringResource(R.string.str_community_floating_main),
    textSky: String = stringResource(R.string.str_community_floating_sub),
    onClick: () -> Unit,
) {
    WableAnnotatedTextButton(
        text = getRequestBoardString(textWhite, textSky),
        onClick = onClick,
        modifier = modifier,
        buttonStyle = BigButtonDefaults.blackBigButtonStyle(),
    )
}

@Composable
private fun getRequestBoardString(
    textWhite: String, textSky: String,
): AnnotatedString {
    val annotatedText = buildAnnotatedString {
        append(textWhite)
        withStyle(style = SpanStyle(color = WableTheme.colors.sky50)) {
            append(textSky)
        }
    }
    return annotatedText
}
