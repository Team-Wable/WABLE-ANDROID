package com.teamwable.ui.extensions

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

/*
Sets a divider between items in the RecyclerView.
Adds padding(76) below the divider for the last item.
 */
fun RecyclerView.setDivider(@DrawableRes drawableRes: Int) {
    addItemDecoration(object : RecyclerView.ItemDecoration() {
        private val divider = ContextCompat.getDrawable(context, drawableRes)

        override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
            for (i in 0 until parent.childCount) {
                val child = parent.getChildAt(i)
                val params = child.layoutParams as RecyclerView.LayoutParams

                val top = child.bottom + params.bottomMargin
                val bottom = top + (divider?.intrinsicHeight ?: 0)
                divider?.setBounds(parent.paddingLeft, top, parent.width - parent.paddingRight, bottom)
                divider?.draw(c)
            }
        }

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            val position = parent.getChildAdapterPosition(view)
            val itemCount = parent.adapter?.itemCount ?: 0

            outRect.bottom = divider?.intrinsicHeight ?: 0

            if (position == itemCount - 1)
                outRect.bottom += view.context.resources.getDimensionPixelOffset(com.teamwable.common.R.dimen.padding_bottom)
        }
    })
}
