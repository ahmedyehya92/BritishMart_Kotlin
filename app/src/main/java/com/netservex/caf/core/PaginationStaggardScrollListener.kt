package com.netservex.caf.core

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

abstract class PaginationStaggardScrollListener(internal var layoutManager: androidx.recyclerview.widget.StaggeredGridLayoutManager) :
    androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
    private var pastVisibleItems: Int = 0
    private var visibleItemCount: Int = 0
    private var totalItemCount: Int = 0

    abstract val totalPageCount: Int

    abstract val isLastPage: Boolean

    abstract val isLoading: Boolean

    override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        visibleItemCount = layoutManager.childCount
        totalItemCount = layoutManager.itemCount
        var firstVisibleItems: IntArray? = null
        firstVisibleItems = layoutManager.findFirstVisibleItemPositions(firstVisibleItems)
        if (firstVisibleItems != null && firstVisibleItems.size > 0) {
            pastVisibleItems = firstVisibleItems[0]
        }


        if (!isLoading && !isLastPage) {
            if (visibleItemCount + pastVisibleItems >= totalItemCount) {

                loadMoreItems()
            }
        }
    }



    protected abstract fun loadMoreItems()
}

