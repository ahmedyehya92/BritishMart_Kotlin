package com.netservex.caf.customviews

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.util.AttributeSet
import android.view.View

class CustomRecyclerView : androidx.recyclerview.widget.RecyclerView {
    private var mEmptyView: View? = null
    internal val observer: androidx.recyclerview.widget.RecyclerView.AdapterDataObserver = object : androidx.recyclerview.widget.RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            super.onChanged()
            initEmptyView()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            super.onItemRangeInserted(positionStart, itemCount)
            initEmptyView()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            super.onItemRangeRemoved(positionStart, itemCount)
            initEmptyView()
        }
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    private fun initEmptyView() {
        if (mEmptyView != null) {
            mEmptyView!!.visibility = if (adapter == null || adapter!!.itemCount == 0) View.VISIBLE else View.GONE
            this@CustomRecyclerView.visibility =
                if (adapter == null || adapter!!.itemCount == 0) View.GONE else View.VISIBLE
        }
    }

    override fun setAdapter(adapter: androidx.recyclerview.widget.RecyclerView.Adapter<*>?) {
        val oldAdapter = getAdapter()
        super.setAdapter(adapter)
        oldAdapter?.unregisterAdapterDataObserver(observer)
        adapter?.registerAdapterDataObserver(observer)
    }

    fun setEmptyView(view: View) {
        this.mEmptyView = view
        initEmptyView()
    }



}