package com.sealstudios.spacex.ui.adapters.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView


class LaunchesListDividerDecoration constructor(
    resId: Int, context: Context,
    private val margin: Int,
) : RecyclerView.ItemDecoration() {

    private val divider: Drawable? = ContextCompat.getDrawable(context, resId)

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {

        val left: Int = parent.paddingLeft + (margin * 4)
        val right: Int = parent.width - parent.paddingRight - (margin * 4)

        val childCount: Int = parent.childCount

        for (i in 0 until childCount) {
            val child: View = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            if (i != childCount - 1) {
                divider?.let {
                    val top = child.bottom + params.bottomMargin
                    val bottom = top + divider.intrinsicHeight
                    it.setBounds(left, top, right, bottom)
                    it.draw(c)
                }
            }
        }
    }

}