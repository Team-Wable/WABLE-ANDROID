package com.teamwable.homedetail

import android.content.Context
import android.graphics.Canvas
import androidx.recyclerview.widget.RecyclerView
import com.teamwable.ui.extensions.drawableOf

class HomeDetailRecyclerViewDivider(
    private val context: Context,
) : RecyclerView.ItemDecoration() {
    private val firstItemDivider = context.drawableOf(com.teamwable.ui.R.drawable.recyclerview_item_8_divider)
    private val otherItemsDivider = context.drawableOf(com.teamwable.ui.R.drawable.recyclerview_item_1_divider)

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin
            val bottom = top + (if (i == 0) firstItemDivider!!.intrinsicHeight else otherItemsDivider!!.intrinsicHeight)

            val divider = if (i == 0) firstItemDivider else otherItemsDivider
            divider?.setBounds(left, top, right, bottom)
            divider?.draw(c)
        }
    }
}
