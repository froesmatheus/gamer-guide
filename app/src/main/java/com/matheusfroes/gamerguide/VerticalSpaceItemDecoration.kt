package com.matheusfroes.gamerguide

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View


/**
 * Created by matheus_froes on 22/09/2017.
 */
class VerticalSpaceItemDecoration(private val verticalSpaceHeight: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
                                state: RecyclerView.State) {
        if (parent.getChildAdapterPosition(view) != parent.adapter.itemCount - 1) {
            outRect.bottom = verticalSpaceHeight
        }
    }
}