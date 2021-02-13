package com.sealstudios.spacex.ui.adapters.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class LaunchesListDecoration constructor(private val margin: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val childCount = parent.adapter!!.itemCount

        outRect.top = margin
        outRect.left = margin
        outRect.right = margin

        if (parent.getChildAdapterPosition(view) == childCount) {
            outRect.bottom = margin
        }

    }
}