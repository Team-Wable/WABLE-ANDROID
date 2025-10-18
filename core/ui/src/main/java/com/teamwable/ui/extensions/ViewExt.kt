package com.teamwable.ui.extensions

import android.view.View
import androidx.annotation.ColorRes
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams

fun View.visible(isVisible: Boolean) {
    this.visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun View.setStatusBarColor(@ColorRes resId: Int) {
    setBackgroundResource(resId)

    ViewCompat.setOnApplyWindowInsetsListener(this.rootView) { _, windowInsets ->
        val insets = windowInsets.getInsets(WindowInsetsCompat.Type.statusBars())
        updateLayoutParams {
            height = insets.top
        }
        windowInsets
    }
}
