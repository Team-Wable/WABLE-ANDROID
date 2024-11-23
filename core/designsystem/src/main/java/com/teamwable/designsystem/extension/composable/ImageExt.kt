package com.teamwable.designsystem.extension.composable

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource

@Composable
fun toImageVector(@DrawableRes id: Int): ImageVector = ImageVector.vectorResource(id = id)
