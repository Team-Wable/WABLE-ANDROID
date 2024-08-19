package com.teamwable.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.teamwable.designsystem.R

val PretendardRegular = FontFamily(Font(R.font.pretendard_regular, FontWeight.Normal))
val PretendardSemiBold = FontFamily(Font(R.font.pretendard_semibold, FontWeight.SemiBold))

@Stable
class WableTypography internal constructor(
    head00: TextStyle,
    head01: TextStyle,
    head02: TextStyle,
    body01: TextStyle,
    body02: TextStyle,
    body03: TextStyle,
    body04: TextStyle,
    caption01: TextStyle,
    caption02: TextStyle,
    caption03: TextStyle,
    caption04: TextStyle,
) {
    var head00: TextStyle by mutableStateOf(head00)
        private set
    var head01: TextStyle by mutableStateOf(head01)
        private set
    var head02: TextStyle by mutableStateOf(head02)
        private set
    var body01: TextStyle by mutableStateOf(body01)
        private set
    var body02: TextStyle by mutableStateOf(body02)
        private set
    var body03: TextStyle by mutableStateOf(body03)
        private set
    var body04: TextStyle by mutableStateOf(body04)
        private set
    var caption01: TextStyle by mutableStateOf(caption01)
        private set
    var caption02: TextStyle by mutableStateOf(caption02)
        private set
    var caption03: TextStyle by mutableStateOf(caption03)
        private set
    var caption04: TextStyle by mutableStateOf(caption04)
        private set

    fun copy(
        head00: TextStyle = this.head00,
        head01: TextStyle = this.head01,
        head02: TextStyle = this.head02,
        body01: TextStyle = this.body01,
        body02: TextStyle = this.body02,
        body03: TextStyle = this.body03,
        body04: TextStyle = this.body04,
        caption01: TextStyle = this.caption01,
        caption02: TextStyle = this.caption02,
        caption03: TextStyle = this.caption03,
        caption04: TextStyle = this.caption04,
    ): WableTypography = WableTypography(
        head00,
        head01,
        head02,
        body01,
        body02,
        body03,
        body04,
        caption01,
        caption02,
        caption03,
        caption04,
    )

    fun update(other: WableTypography) {
        head00 = other.head00
        head01 = other.head01
        head02 = other.head02
        body01 = other.body01
        body02 = other.body02
        body03 = other.body03
        body04 = other.body04
        caption01 = other.caption01
        caption02 = other.caption02
        caption03 = other.caption03
        caption04 = other.caption04
    }
}

fun wableTextStyle(
    fontFamily: FontFamily,
    fontWeight: FontWeight,
    fontSize: TextUnit,
    letterSpacing: TextUnit,
    lineHeight: TextUnit,
): TextStyle = TextStyle(
    fontFamily = fontFamily,
    fontWeight = fontWeight,
    fontSize = fontSize,
    lineHeight = lineHeight,
    letterSpacing = letterSpacing,
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None,
    ),
)

@Composable
fun wableTypography(): WableTypography {
    return WableTypography(
        head00 = wableTextStyle(
            fontFamily = PretendardSemiBold,
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
            lineHeight = 1.6.em,
            letterSpacing = (-0.01).em,
        ),
        head01 = wableTextStyle(
            fontFamily = PretendardSemiBold,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            lineHeight = 1.6.em,
            letterSpacing = (-0.01).em,
        ),
        head02 = wableTextStyle(
            fontFamily = PretendardRegular,
            fontWeight = FontWeight.SemiBold,
            fontSize = 22.sp,
            lineHeight = 1.6.em,
            letterSpacing = (-0.01).em,
        ),
        body01 = wableTextStyle(
            fontFamily = PretendardSemiBold,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            lineHeight = 1.6.em,
            letterSpacing = (-0.01).em,
        ),
        body02 = wableTextStyle(
            fontFamily = PretendardRegular,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 1.6.em,
            letterSpacing = (-0.01).em,
        ),
        body03 = wableTextStyle(
            fontFamily = PretendardSemiBold,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            lineHeight = 1.6.em,
            letterSpacing = (-0.01).em,
        ),
        body04 = wableTextStyle(
            fontFamily = PretendardRegular,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 1.6.em,
            letterSpacing = (-0.01).em,
        ),
        caption01 = wableTextStyle(
            fontFamily = PretendardSemiBold,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            lineHeight = 1.6.em,
            letterSpacing = (-0.01).em,
        ),
        caption02 = wableTextStyle(
            fontFamily = PretendardRegular,
            fontWeight = FontWeight.Normal,
            fontSize = 13.sp,
            lineHeight = 1.6.em,
            letterSpacing = (-0.01).em,
        ),
        caption03 = wableTextStyle(
            fontFamily = PretendardSemiBold,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            lineHeight = 1.6.em,
            letterSpacing = (-0.01).em,
        ),
        caption04 = wableTextStyle(
            fontFamily = PretendardRegular,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 1.6.em,
            letterSpacing = (-0.01).em,
        ),
    )
}
