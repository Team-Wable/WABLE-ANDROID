package com.teamwable.profile.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupPositionProvider
import com.teamwable.designsystem.extension.composable.toImageVector
import com.teamwable.designsystem.extension.modifier.noRippleClickable
import com.teamwable.designsystem.theme.WableTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GhostInfoTooltip(
    modifier: Modifier = Modifier,
) {
    val tooltipState = rememberTooltipState()
    val scope = rememberCoroutineScope()

    TooltipBox(
        modifier = modifier,
        positionProvider = createCustomPositionProvider(19.dp),
        tooltip = {
            Column(
                modifier = Modifier
                    .padding(start = 16.dp, end = 82.dp),
            ) {
                Icon(
                    imageVector = toImageVector(
                        id = com.teamwable.common.R.drawable.ic_tooltip_triangle,
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .offset(y = 4.dp),
                )

                Text(
                    text = createTooltipText(),
                    color = WableTheme.colors.gray200,
                    style = WableTheme.typography.caption03,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(WableTheme.colors.gray900)
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                )
            }
        },
        state = tooltipState,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "투명도",
                style = WableTheme.typography.caption01,
            )

            Spacer(modifier = Modifier.width(2.dp))

            Icon(
                imageVector = toImageVector(
                    id = com.teamwable.common.R.drawable.ic_info,
                ),
                tint = Color.Unspecified,
                contentDescription = "투명도 툴팁 보기",
                modifier = Modifier.noRippleClickable {
                    scope.launch {
                        if (tooltipState.isVisible) {
                            tooltipState.dismiss()
                        } else {
                            tooltipState.show()
                        }
                    }
                },
            )
        }
    }
}

@Composable
fun createCustomPositionProvider(spacing: Dp = 8.dp): PopupPositionProvider {
    val density = LocalDensity.current

    return remember(spacing, density) {
        object : PopupPositionProvider {
            override fun calculatePosition(
                anchorBounds: IntRect,
                windowSize: IntSize,
                layoutDirection: LayoutDirection,
                popupContentSize: IntSize,
            ): IntOffset {
                val spacingPx = with(density) { spacing.roundToPx() }
                val x = anchorBounds.left
                val y = anchorBounds.bottom + spacingPx

                return IntOffset(x, y)
            }
        }
    }
}

@Composable
fun createTooltipText(): AnnotatedString {
    return buildAnnotatedString {
        withStyle(style = SpanStyle(color = WableTheme.colors.sky50)) {
            append("투명도란? ")
        }

        withStyle(style = SpanStyle(color = WableTheme.colors.gray200)) {
            append("설명 어쩌고 저쩌고 어쩌고 저쩌고 어쩌고 저쩌고 어쩌고 저쩌고...")
        }
    }
}

@Preview(
    showBackground = true,
    group = "ProfileComponent",
)
@Composable
private fun GhostInfoTooltipPreview() {
    WableTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
        ) {
            GhostInfoTooltip()
        }
    }
}
