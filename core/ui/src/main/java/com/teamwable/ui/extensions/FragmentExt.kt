package com.teamwable.ui.extensions

import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar

fun Fragment.toast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}

fun Fragment.longToast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
}

fun Fragment.snackBar(
    anchorView: View,
    message: () -> String,
) {
    Snackbar.make(anchorView, message(), Snackbar.LENGTH_SHORT).show()
}

fun Fragment.stringOf(
    @StringRes resId: Int,
) = getString(resId)

fun Fragment.colorOf(
    @ColorRes resId: Int,
) = ContextCompat.getColor(requireContext(), resId)

fun Fragment.drawableOf(
    @DrawableRes resId: Int,
) = ContextCompat.getDrawable(requireContext(), resId)

val Fragment.viewLifeCycle
    get() = viewLifecycleOwner.lifecycle

val Fragment.viewLifeCycleScope
    get() = viewLifecycleOwner.lifecycleScope

fun Fragment.statusBarColorOf(
    @ColorRes resId: Int,
) {
    requireActivity().statusBarColorOf(resId)
}

fun Fragment.openUri(uri: String) {
    Intent(Intent.ACTION_VIEW, Uri.parse(uri)).also {
        runCatching { startActivity(it) }
            .onFailure { Toast.makeText(requireContext(), "URL을 열 수 없습니다.", Toast.LENGTH_SHORT).show() }
    }
}

fun Fragment.statusBarModeOf(isLightStatusBar: Boolean = true) {
    requireActivity().window.apply {
        WindowInsetsControllerCompat(this, decorView).isAppearanceLightStatusBars = isLightStatusBar
    }
}

inline fun <reified T : Enum<T>> Fragment.setupEnumResultListener(
    key: String,
    typeKey: String,
    crossinline onResult: (T) -> Unit,
) {
    parentFragmentManager.setFragmentResultListener(
        key,
        viewLifecycleOwner,
    ) { _, bundle ->
        bundle.getString(typeKey)?.let {
            onResult(enumValueOf<T>(it))
        }
    }
}
