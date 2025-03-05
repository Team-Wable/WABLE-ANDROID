package com.teamwable.designsystem.component.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.teamwable.designsystem.theme.WableTheme

@Composable
fun WableShapeBox(
    modifier: Modifier = Modifier,
    shapeStyle: CustomShapeStyle = singleSharpStyle(),
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .clip(
                RoundedCornerShape(
                    topStart = shapeStyle.topStart,
                    topEnd = shapeStyle.topEnd,
                    bottomStart = shapeStyle.bottomStart,
                    bottomEnd = shapeStyle.bottomEnd,
                ),
            )
            .background(shapeStyle.backgroundColor),
    ) {
        content()
    }
}

@Stable
data class CustomShapeStyle(
    val topStart: Dp = 8.dp,
    val topEnd: Dp = 8.dp,
    val bottomStart: Dp = 8.dp,
    val bottomEnd: Dp = 8.dp,
    val backgroundColor: Color,
)

@Composable
@ReadOnlyComposable
fun defaultShapeStyle() = CustomShapeStyle(
    backgroundColor = WableTheme.colors.purple10,
)

@Composable
@ReadOnlyComposable
fun singleSharpStyle() = defaultShapeStyle().copy(
    topStart = 0.dp,
)

@Composable
@Preview(showBackground = true, backgroundColor = 0xFF000000)
private fun ShapeBoxPreview() {
    WableTheme {
        WableShapeBox {
            Text(
                text = "asdfsf",
                modifier = Modifier.padding(8.dp),
            )
        }
    }
}
