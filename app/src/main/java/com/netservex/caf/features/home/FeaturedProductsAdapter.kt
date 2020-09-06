package com.netservex.caf.features.home

import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.netservex.caf.R
import com.netservex.caf.core.PaginationAdapterCallBack
import com.netservex.entities.ProductModel
import com.wang.avi.AVLoadingIndicatorView
import java.util.ArrayList

class FeaturedProductsAdapter(
    private val context: Context,
    arrayList: ArrayList<ProductModel>?, dataType: Int
) :
    androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder?>() {
    private val arrayList: MutableList<ProductModel>?
    private val dataType: Int
    private var customListener: customButtonListener? = null
    private var isLoadingAdded = false
    private var retryPageLoad = false
    var handler: Handler? = null
    var runnable: Runnable? = null
    private var mCallback: PaginationAdapterCallBack? = null
    private var errorMsg: String? = null
    override fun getItemCount() =  arrayList!!.size

    override fun getItemViewType(position: Int): Int {
        return if (position == arrayList!!.size - 1 && isLoadingAdded) LOADING else ITEM
    }

    fun add(r: ProductModel) {
        arrayList!!.add(r)
        notifyItemInserted(arrayList.size - 1)
    }

    fun addAll(opResults: MutableList<ProductModel>) {
        for (result in opResults) {
            add(result)
        }
    }

    fun remove(r: ProductModel?) {
        val position = arrayList!!.indexOf(r)
        if (position > -1) {
            arrayList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun clear() {
        isLoadingAdded = false
        while (itemCount > 0) {
            remove(getItem(0))
        }
    }

    fun getItem(position: Int): ProductModel {
        return arrayList!![position]
    }

    val isEmpty: Boolean
        get() = itemCount == 0

    fun addLoadingFooter() {
        isLoadingAdded = true
        //add(new OpportunityModel());
        add(getItem(arrayList!!.size - 1))
    }

    fun removeLoadingFooter() {
        if (isLoadingAdded) {

            val position = arrayList!!.size - 1
            val result: ProductModel = getItem(position)
            if (result != null) {
                arrayList.removeAt(position)
                notifyItemRemoved(position)
            }
        }
        isLoadingAdded = false
    }

    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        val model: ProductModel = arrayList!![position]
        when (getItemViewType(position)) {
            ITEM -> {
                val subCategoryViewHolder =
                    holder as SubCategoryViewHolder

                Glide.with(context)
                    .load(model.image)
                    .into(subCategoryViewHolder.img_desc)

                subCategoryViewHolder.tv_title?.text = model.name
                subCategoryViewHolder.lout_container!!.setOnClickListener {
                    customListener!!.onItemFeaturedClickListner(
                        model
                    )
                }
            }
            LOADING -> {
                val loadingVH = holder as LoadingVH
                val layoutParams =
                    loadingVH.itemView.layoutParams as androidx.recyclerview.widget.StaggeredGridLayoutManager.LayoutParams
                layoutParams.isFullSpan = true
                if (retryPageLoad) {
                    holder.footerLayout.visibility = View.VISIBLE
                    holder.mProgressBar.visibility = View.GONE

                } else run {
                    holder.footerLayout.visibility = View.GONE
                    holder.mProgressBar.visibility = View.VISIBLE
                }

                holder.mRetryBtn.setOnClickListener {
                    showRetry(false,"Error!")
                    mCallback!!.retryPageLoad()
                }
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        var viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder? = null
        val mInflater = LayoutInflater.from(viewGroup.context)
        when (viewType) {
            ITEM -> {
                val viewItem: View = mInflater.inflate(
                    R.layout.view_featured_item, viewGroup, false
                )
                viewHolder = SubCategoryViewHolder(viewItem)
            }
            LOADING -> {
                val viewLoading: View =
                    mInflater.inflate(R.layout.view_loading_footer, viewGroup, false)
                viewHolder = LoadingVH(viewLoading)
            }
        }
        return viewHolder!!
    }

    inner class SubCategoryViewHolder(itemView: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView!!) {

        val img_desc by lazy { itemView.findViewById<ImageView>(R.id.im_product) }
        val tv_title by lazy { itemView.findViewById<TextView>(R.id.tv_title) }
        val lout_container by lazy { itemView.findViewById<LinearLayout>(R.id.lout_main) }
    }

    protected inner class LoadingVH(itemView: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView!!) {
        val mProgressBar by lazy { itemView.findViewById<AVLoadingIndicatorView>(R.id.avi_loading_more) }
        val mRetryBtn by lazy { itemView.findViewById<LinearLayout>(R.id.btn_try_again) }
        val footerLayout by lazy { itemView.findViewById<LinearLayout>(R.id.loadmore_errorlayout) }


    }

    interface customButtonListener {
        fun onItemFeaturedClickListner(productModel: ProductModel)
    }

    fun setCustomButtonListner(listener: customButtonListener?) {
        customListener = listener
    }

    fun setPagingAdapterCallback(pagingAdapterCallback: PaginationAdapterCallBack?) {
        mCallback = pagingAdapterCallback
    }

    fun showRetry(show: Boolean, errorMsg: String?) {
        retryPageLoad = show
        notifyItemChanged(arrayList!!.size - 1)
        if (errorMsg != null) this.errorMsg = errorMsg
    }

    companion object {
        private const val ITEM = 0
        private const val LOADING = 1
    }

    init {
        this.arrayList = arrayList
        this.dataType = dataType
    }
}
