package com.teamwable.ui.extensions

import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.setDivider(@DrawableRes drawableRes: Int) {
    val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
    val dividerDrawableRes = ContextCompat.getDrawable(context, drawableRes)
    if (dividerDrawableRes != null) divider.setDrawable(dividerDrawableRes)
    addItemDecoration(divider)
}
