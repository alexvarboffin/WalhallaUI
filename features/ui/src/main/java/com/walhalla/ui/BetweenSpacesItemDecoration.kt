package com.walhalla.ui

import android.content.res.Resources
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class BetweenSpacesItemDecoration(verticalSpace: Int, horizontalSpace: Int) :
    RecyclerView.ItemDecoration() {
    private val mVerticalSpace: Int
    private val mHorizontalSpace: Int

    init {
        mVerticalSpace = dpToPixels(verticalSpace.toFloat())
        mHorizontalSpace = dpToPixels(horizontalSpace.toFloat())
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildViewHolder(view).adapterPosition
        val itemCount = state.itemCount
        val layoutManager = parent.layoutManager
        setSpacingForDirection(outRect, layoutManager!!, position, itemCount)
    }

    /* https://gist.github.com/alexfu/f7b8278009f3119f523a */
    private fun setSpacingForDirection(
        outRect: Rect,
        layoutManager: RecyclerView.LayoutManager,
        position: Int,
        itemCount: Int
    ) {
        /* Resolve display mode automatically */
        val displayMode = resolveDisplayMode(layoutManager)

        if (displayMode == HORIZONTAL) {
            outRect.left = 0
            outRect.right = if (position == itemCount - 1) 0 else mHorizontalSpace
            outRect.top = 0
            outRect.bottom = 0
        } else if (displayMode == VERTICAL) {
            outRect.left = 0
            outRect.right = 0
            outRect.top = 0
            outRect.bottom = if (position == itemCount - 1) 0 else mVerticalSpace
        } else if (displayMode == GRID) {
            val gridLayoutManager = layoutManager as GridLayoutManager
            val cols = gridLayoutManager.spanCount
            val rows = itemCount / cols

            outRect.left = if (position % cols == 0) 0 else mHorizontalSpace / 2
            outRect.right = if ((position + 1) % cols == 0) 0 else mHorizontalSpace / 2
            outRect.top = if (position < cols) 0 else mVerticalSpace / 2
            outRect.bottom = if (position / cols == rows) 0 else mVerticalSpace / 2
        }
    }

    private fun resolveDisplayMode(layoutManager: RecyclerView.LayoutManager): Int {
        if (layoutManager is GridLayoutManager) {
            return GRID
        }
        if (layoutManager.canScrollHorizontally()) {
            return HORIZONTAL
        }
        return VERTICAL
    }

    companion object {
        private const val HORIZONTAL = 0
        private const val VERTICAL = 1
        private const val GRID = 2

        fun dpToPixels(dp: Float): Int {
            return (dp * Resources.getSystem().displayMetrics.density).toInt()
        }
    }
}