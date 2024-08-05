package com.teamwable.ui.extensions

import android.view.View

fun View.visible(isVisible: Boolean) {
    this.visibility = if (isVisible) View.VISIBLE else View.GONE
}
