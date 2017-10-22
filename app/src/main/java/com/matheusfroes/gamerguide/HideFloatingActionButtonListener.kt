package com.matheusfroes.gamerguide

import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.AbsListView


/**
 * Hides the floating action button when scrolling down, shows it when scrolling up. If the end of
 * the list will be reached, shows the button also.
 *
 *
 * Built upon https://github.com/makovkastar/FloatingActionButton scroll detectors.
 */
class HideFloatingActionButtonListener(button: FloatingActionButton) : RecyclerView.OnScrollListener() {
    private var scrollListener: AbsListView.OnScrollListener? = null
    private var lastFirstVisible = -1
    private var lastVisibleCount = -1
    private var lastItemCount = -1

    init {
        scrollListener = FabAbsListViewScrollDetector(button)
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager

        val firstVisible = layoutManager.findFirstVisibleItemPosition()
        val visibleCount = Math.abs(firstVisible - layoutManager.findLastVisibleItemPosition())
        val itemCount = recyclerView.adapter.itemCount

        if (firstVisible != lastFirstVisible || visibleCount != lastVisibleCount || itemCount != lastItemCount) {
            scrollListener?.onScroll(null, firstVisible, visibleCount, itemCount)
            lastFirstVisible = firstVisible
            lastVisibleCount = visibleCount
            lastItemCount = itemCount
        }
    }

}

class FabAbsListViewScrollDetector(private val button: FloatingActionButton) : AbsListView.OnScrollListener {
    private var lastScrollY: Int = 0
    private var previousFirstVisibleItem: Int = 0
    private val scrollThreshold: Int = button.context
            .resources
            .getDimensionPixelOffset(R.dimen.fab_scroll_threshold)

    override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {}

    override fun onScroll(view: AbsListView, firstVisibleItem: Int, visibleItemCount: Int,
                          totalItemCount: Int) {
        if (!view.hasFocus() || totalItemCount == 0) {
            return
        }

        // always show if scrolled to bottom
        if (firstVisibleItem + visibleItemCount == totalItemCount) {
            button.show()
            return
        }

        // still on the same row?
        if (firstVisibleItem == previousFirstVisibleItem) {
            val newScrollY = getTopItemScrollY(view)
            val isSignificantDelta = Math.abs(lastScrollY - newScrollY) > scrollThreshold
            if (isSignificantDelta) {
                if (lastScrollY > newScrollY) {
                    button.hide()
                } else {
                    button.show()
                }
            }
            lastScrollY = newScrollY
        } else {
            if (firstVisibleItem > previousFirstVisibleItem) {
                button.hide()
            } else {
                button.show()
            }

            lastScrollY = getTopItemScrollY(view)
            previousFirstVisibleItem = firstVisibleItem
        }
    }

    private fun getTopItemScrollY(view: AbsListView?): Int {
        if (view == null) {
            return 0
        }
        val topChild = view.getChildAt(0) ?: return 0
        return topChild.top
    }
}

