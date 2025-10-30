package com.teamwable.ui.component

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Animatable
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.teamwable.common.util.getThrowableMessage
import com.teamwable.ui.R
import com.teamwable.ui.databinding.SnackbarBinding
import com.teamwable.ui.extensions.drawableOf
import com.teamwable.ui.extensions.stringOf
import com.teamwable.ui.type.SnackbarType

class Snackbar(private val view: View, private val type: SnackbarType, private val throwable: Throwable? = null) {
    private val context = view.context
    private val snackbar = Snackbar.make(view, "", 10000)
    private val snackbarView = snackbar.view as ViewGroup

    private val inflater = LayoutInflater.from(context)
    private val binding: SnackbarBinding = SnackbarBinding.inflate(inflater, snackbarView, false)
    private val params = snackbarView.layoutParams as FrameLayout.LayoutParams

    init {
        initView()
    }

    private fun initView() {
        setSnackbarView()
        initLayout(type, throwable)
        snackbarView.addView(binding.root)
    }

    private fun setSnackbarView() = with(snackbarView) {
        removeAllViews()
        setBackgroundColor(Color.TRANSPARENT)
        params.gravity = Gravity.TOP
        setPadding(18, 30, 18, 30)
        clipChildren = false
        clipToPadding = false
    }

    private fun initLayout(type: SnackbarType, throwable: Throwable?) = binding.tvSnackbarMessage.apply {
        val iconDrawable = context.drawableOf(type.icon)
        text = throwable?.getThrowableMessage() ?: context.stringOf(type.message)
        background = ContextCompat.getDrawable(context, R.drawable.shape_white_fill_8_rect)
        backgroundTintList = ColorStateList.valueOf(Color.parseColor("#E6F7F7F7"))

        setCompoundDrawablesWithIntrinsicBounds(iconDrawable, null, null, null)
        if (iconDrawable is Animatable) iconDrawable.start()
    }

    fun show() {
        snackbar.show()
        if (type !in listOf(
                SnackbarType.COMMENT_ING,
                SnackbarType.COMMENT_COMPLETE,
                SnackbarType.CHILD_COMMENT_ING,
                SnackbarType.CHILD_COMMENT_COMPLETE,
            )
        ) dismissSnackbar(2000)
    }

    private fun dismissSnackbar(duration: Long) {
        Handler(Looper.getMainLooper()).postDelayed({
            snackbar.dismiss()
        }, duration)
    }

    fun updateToCommentComplete(type: SnackbarType, throwable: Throwable? = null) {
        initLayout(type, throwable)
        dismissSnackbar(1000)
    }

    companion object {
        fun make(view: View, type: SnackbarType, throwable: Throwable? = null) = Snackbar(view, type, throwable)
    }
}
