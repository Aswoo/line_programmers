package com.test.memoapp.util

import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class HorizentalRecyclerViewItemDecoration constructor(context: Context?) :
    RecyclerView.ItemDecoration() {

    private val PADDING_IN_DIPS = 16
    private var mPadding: Int = 0

    init {

        val metrics = context!!.resources.displayMetrics
        mPadding = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            PADDING_IN_DIPS.toFloat(),
            metrics
        ).toInt()
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val itemCount = state.itemCount

        val itemPosition = parent.getChildAdapterPosition(view)

        if (itemPosition == RecyclerView.NO_POSITION) {
            return
        }

        outRect.left = mPadding/2
        outRect.right = mPadding/2

    }
}

