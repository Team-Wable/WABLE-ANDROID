package com.teamwable.designsystem.component.snackbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamwable.designsystem.R
import com.teamwable.designsystem.component.indicator.WableCircularIndicator
import com.teamwable.designsystem.extension.modifier.dropShadow
import com.teamwable.designsystem.theme.WableTheme
import com.teamwable.designsystem.type.SnackBarType

const val SNACK_BAR_DURATION = 2000L

@Composable
fun WableSnackBar(
    modifier: Modifier = Modifier,
    message: String = "",
    snackBarType: SnackBarType = SnackBarType.ERROR,
    onClick: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .padding(horizontal = 18.dp)
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = WableTheme.colors.gray200,
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.radius_8)),
            )
            .dropShadow(
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.radius_8)),
                color = Color.Black.copy(alpha = 0.12f),
                blur = 4.dp,
                offsetX = 0.dp,
                offsetY = 2.dp,
                spread = 0.dp,
            )
            .background(
                color = WableTheme.colors.gray100.copy(alpha = 0.9f),
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.radius_8)),
            ),
        contentAlignment = Alignment.CenterStart,
    ) {
        WableSnackBarContent(
            snackBarType = snackBarType,
            message = message,
        )
    }
}

@Composable
fun WableSnackBarContent(
    snackBarType: SnackBarType,
    message: String,
) {
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        when (snackBarType) {
            SnackBarType.LOADING -> WableCircularIndicator()
            else -> WableSnackBarImage(snackBarType)
        }
        Text(
            text = when (snackBarType) {
                SnackBarType.LOADING -> stringResource(id = R.string.snackbar_text_loading)
                SnackBarType.LOADING_PROFILE -> stringResource(id = R.string.snackbar_text_profile_loading)
                else -> message
            },
            textAlign = TextAlign.Start,
            color = WableTheme.colors.black,
            style = WableTheme.typography.body03,
            modifier = Modifier.padding(start = 6.dp),
        )
    }
}

@Composable
fun WableSnackBarImage(snackBarType: SnackBarType) {
    Image(
        painter = painterResource(id = snackBarType.image),
        contentDescription = null,
    )
}

@Preview(showBackground = true)
@Composable
fun WableButtonDialogPreview() {
    WableTheme {
        WableSnackBar(
            snackBarType = SnackBarType.SUCCESS,
            message = "프로필 사진을 5MB 이하인 사진으로 바꿔주세요",
        )
    }
}
