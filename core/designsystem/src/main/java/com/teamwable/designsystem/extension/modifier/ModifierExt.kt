package com.teamwable.designsystem.extension.modifier

import android.graphics.BlurMaskFilter
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

inline fun Modifier.noRippleClickable(
    crossinline onClick: () -> Unit = {},
): Modifier = composed {
    this.clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() },
    ) {
        onClick()
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Modifier.noRippleDebounceClickable(
    onClick: () -> Unit,
): Modifier = composed {
    var clickable by remember { mutableStateOf(true) }

    pointerInteropFilter {
        if (clickable) {
            onClick()
            clickable = false
            CoroutineScope(Dispatchers.Main).launch {
                delay(500) // 500밀리초 딜레이
                clickable = true
            }
        }
        true
    }
}

internal interface MultipleEventsCutter {
    fun processEvent(event: () -> Unit)

    companion object
}

internal fun MultipleEventsCutter.Companion.get(): MultipleEventsCutter =
    MultipleEventsCutterImpl()

private class MultipleEventsCutterImpl : MultipleEventsCutter {
    private val now: Long
        get() = System.currentTimeMillis()

    private var lastEventTimeMs: Long = 0

    override fun processEvent(event: () -> Unit) {
        if (now - lastEventTimeMs >= 300L) {
            event.invoke()
        }
        lastEventTimeMs = now
    }
}

fun Modifier.noRippleThrottleClickable(
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit,
) = composed(
    inspectorInfo = debugInspectorInfo {
        name = "clickable"
        properties["enabled"] = enabled
        properties["onClickLabel"] = onClickLabel
        properties["role"] = role
        properties["onClick"] = onClick
    },
) {
    val multipleEventsCutter = remember { MultipleEventsCutter.get() }
    Modifier.clickable(
        enabled = enabled,
        onClickLabel = onClickLabel,
        onClick = { multipleEventsCutter.processEvent { onClick() } },
        role = role,
        indication = null,
        interactionSource = remember { MutableInteractionSource() },
    )
}

@Composable
fun Modifier.dropShadow(
    shape: Shape,
    color: Color = Color.Black.copy(alpha = 0.25f),
    blur: Dp = 4.dp,
    offsetY: Dp = 4.dp,
    offsetX: Dp = 0.dp,
    spread: Dp = 0.dp,
) = this.drawBehind {
    val spreadPx = spread.toPx()
    val shadowSize = Size(size.width + spreadPx, size.height + spreadPx)

    val shadowOutline = shape.createOutline(shadowSize, layoutDirection, this)

    val paint = Paint().apply {
        this.color = color
    }

    if (blur.toPx() > 0) {
        paint.asFrameworkPaint().apply {
            maskFilter = BlurMaskFilter(blur.toPx(), BlurMaskFilter.Blur.NORMAL)
        }
    }

    drawIntoCanvas { canvas ->
        canvas.save()
        canvas.translate(offsetX.toPx() - spreadPx / 2, offsetY.toPx() - spreadPx / 2)
        canvas.drawOutline(shadowOutline, paint)
        canvas.restore()
    }
}

fun Modifier.showIf(condition: Boolean): Modifier =
    if (condition) this else Modifier.size(0.dp)

val WableGradientColorStops = arrayOf(
    0.0f to Color(0xFFEBE2FD),
    0.37f to Color(0xFFF0F6FE),
    0.69f to Color(0xFFF7FEFD),
    1f to Color(0xFFFDFDFD),
)

// ✅ 2. Modifier 확장 함수로 그라데이션 적용
@Composable
fun Modifier.wableVerticalGradientBackground(
    ratio: Float = 0.514f,
    colorStops: Array<Pair<Float, Color>> = WableGradientColorStops,
): Modifier {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val screenHeightPx = with(density) { configuration.screenHeightDp.dp.toPx() }
    val gradientEndY = screenHeightPx * ratio

    return this.background(
        brush = Brush.verticalGradient(
            colorStops = colorStops,
            tileMode = TileMode.Decal,
            startY = 0f,
            endY = gradientEndY,
        ),
    )
}
