package com.netservex.caf.features.products_list

import android.content.Context
import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
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

class AdapterProductsList(
    private val context: Context,
    arrayList: ArrayList<ProductModel>?, dataType: Int
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
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
        isLoadingAdded = false
        val position = arrayList!!.size - 1
        val result: ProductModel = getItem(position)
        if (result != null) {
            arrayList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
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
                    customListener!!.onItemClickListner(
                        model.id,
                        model.name
                    )
                }
            }
            LOADING -> {
                val loadingVH = holder as LoadingVH
                val layoutParams =
                    loadingVH.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
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

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder: RecyclerView.ViewHolder? = null
        val mInflater = LayoutInflater.from(viewGroup.context)
        when (viewType) {
            ITEM -> {
                val viewItem: View = mInflater.inflate(
                    R.layout.view_product_item_grid, viewGroup, false
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
        RecyclerView.ViewHolder(itemView!!) {

        val img_desc by lazy { itemView.findViewById<ImageView>(R.id.cat_home_big_img) }
        val tv_title by lazy { itemView.findViewById<TextView>(R.id.cat_home_big_txt) }
        val lout_container by lazy { itemView.findViewById<RelativeLayout>(R.id.cat_home_big_container) }
    }

    protected inner class LoadingVH(itemView: View) :
        RecyclerView.ViewHolder(itemView!!) {
        val mProgressBar by lazy { itemView.findViewById<AVLoadingIndicatorView>(R.id.avi_loading_more) }
        val mRetryBtn by lazy { itemView.findViewById<LinearLayout>(R.id.btn_try_again) }
        val footerLayout by lazy { itemView.findViewById<LinearLayout>(R.id.loadmore_errorlayout) }


    }

    interface customButtonListener {
        fun onItemClickListner(id: String?, title: String?)
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
