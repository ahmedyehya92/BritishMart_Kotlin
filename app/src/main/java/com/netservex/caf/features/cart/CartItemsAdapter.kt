package com.netservex.caf.features.cart

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.netservex.caf.R
import com.netservex.caf.core.PaginationAdapterCallBack
import com.netservex.entities.CartItemModel
import com.wang.avi.AVLoadingIndicatorView

class CartItemsAdapter (
    private val cartItemsList: MutableList<CartItemModel>,
    private val context: Context? = null,
    private val ITEM: Int = 0,
    private val LOADING: Int = 1,
    private var isLoadingAdded: Boolean = false,
    var retryPageLoad: Boolean = false,
    private var mCallback: PaginationAdapterCallBack? = null
) : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    private var customeListener: CustomeListener? = null

    override fun getItemCount() = cartItemsList.size

    override fun getItemViewType(position: Int): Int {
        return if (position == cartItemsList.size - 1 && isLoadingAdded) LOADING else ITEM
    }

    fun getItems() = cartItemsList



    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        val mInflater = LayoutInflater.from(p0.context)

        when (p1) {
            ITEM -> run {
                Log.d("", "loooooading ITEM")
                return CartItemsListItemViewHolder(
                    mInflater.inflate(R.layout.view_item_cart, p0, false)
                )
            }

            LOADING -> kotlin.run {
                Log.d("", "loooooading LOADING")
                return ListLoadingFooterViewHolder(
                    mInflater.inflate(R.layout.view_loading_footer, p0, false)
                )
            }

            else -> run {
                Log.d("", "loooooading else")

                return CartItemsListItemViewHolder(
                    mInflater.inflate(R.layout.view_item_cart, p0, false)
                )
            }
        }
    }


    override fun onBindViewHolder(p0: androidx.recyclerview.widget.RecyclerView.ViewHolder, p1: Int) {
        val cartItem: CartItemModel = cartItemsList[p1]
        if (getItemViewType(p1) == ITEM) {
            val itemViewHolder = p0 as CartItemsListItemViewHolder

            itemViewHolder.tvProductName.setText(cartItem.name)
            itemViewHolder.tvQuntity.setText(java.lang.String.valueOf(cartItem.quantity))
            itemViewHolder.btnDecreaseQuantity.setOnClickListener(View.OnClickListener {
                if (cartItemsList.get(p1).quantity !== 1) {
                    cartItemsList.get(p1).quantity = (cartItemsList.get(p1).quantity?.minus(1))
                    customeListener!!.onAmountEditListener(
                        cartItemsList.get(p1).quantity,
                        p1
                    )
                }
            })

            itemViewHolder.btnIncreaseAuantity.setOnClickListener(View.OnClickListener {
                if (cartItemsList.get(p1).quantity !== 10) {
                    cartItemsList[p1].quantity = cartItemsList[p1].quantity?.plus(1)
                    customeListener!!.onAmountEditListener(
                        cartItemsList[p1].quantity,
                        p1
                    )
                }
            })

            itemViewHolder.btnDelete.setOnClickListener(View.OnClickListener {
                customeListener!!.onRemoveButtonClickListner(
                    cartItem.id,
                    p1
                )
            })

            if (!(cartItem.image == null || cartItem.image.equals(""))) {
                Glide.with(context)
                    .load(cartItem.image)
                    .into(itemViewHolder.imvDescription)
            } else { /* Glide.with(context)
                    .load(R.drawable.placeholder).diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(viewHolder.imvDescription);*/
            }

            cartItem.priceOfUnit?.let { cartItem.priceForTotalQuantity =  cartItem.quantity?.times(it)   }

            itemViewHolder.tvTotalQuantityPrice.text = cartItem.priceForTotalQuantity.toString()



            //itemViewHolder.tvTotalQuantityPrice.setText(java.lang.String.valueOf(cartItem.priceForTotalQuantity))

            itemViewHolder.loutContainer.setOnClickListener(View.OnClickListener {
                customeListener!!.onItemClickListener(
                    cartItem.id
                )
            })
        }

        else if (getItemViewType(p1) == LOADING)
        {
            p0 as ListLoadingFooterViewHolder

            Log.d("", "loooooading")



            if (retryPageLoad) {
                p0.footerLayout.visibility = View.VISIBLE
                p0.mProgressBar.visibility = View.GONE

            } else run {
                p0.footerLayout.visibility = View.GONE
                p0.mProgressBar.visibility = View.VISIBLE
            }

            p0.mRetryBtn.setOnClickListener {
                showRetry(false)
                mCallback!!.retryPageLoad()
            }
        }
    }


    interface CustomeListener {
        fun onRemoveButtonClickListner(productId: String?, position: Int)
        fun onAmountEditListener(currentQuantity: Int, position: Int)
        fun onItemClickListener(productId: String?)
    }

    fun setCustomButtonListner(listener: CustomeListener) {
        this.customeListener = listener
    }

    class ListLoadingFooterViewHolder(private val view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

        val mProgressBar by lazy { view.findViewById<AVLoadingIndicatorView>(R.id.avi_loading_more) }

        val mRetryBtn by lazy { view.findViewById<LinearLayout>(R.id.btn_try_again) }

        val footerLayout by lazy { view.findViewById<LinearLayout>(R.id.loadmore_errorlayout) }

    }

    class CartItemsListItemViewHolder(private val view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val imvDescription by lazy { view.findViewById<ImageView>(R.id.imageView3) }
        val loutContainer by lazy { view.findViewById<ConstraintLayout>(R.id.lout_cont) }
        val tvProductName by lazy { view.findViewById<TextView>(R.id.tv_product_name) }
        val tvQuntity by lazy { view.findViewById<TextView>(R.id.tv_quantity) }
        val btnDecreaseQuantity by lazy { view.findViewById<ImageView>(R.id.btn_decrease) }
        val btnIncreaseAuantity by lazy { view.findViewById<ImageView>(R.id.btn_increase) }
        val tvTotalQuantityPrice by lazy { view.findViewById<TextView>(R.id.tv_total_quantity_price) }
        val btnDelete by lazy { view.findViewById<ImageView>(R.id.imageView4) }
    }


    fun showRetry(show: Boolean) {
        retryPageLoad = show
        notifyItemChanged(cartItemsList.size - 1)
    }

    fun remove(r: CartItemModel) {
        val position = cartItemsList.indexOf(r)
        if (position > -1) {
            cartItemsList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun removeAtPosition(position: Int)
    {
        cartItemsList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun clear() {
        isLoadingAdded = false
        while (itemCount > 0) {
            Log.d("","getItem:clear")
            remove(getItem(0))
        }
    }

    fun getItem(position: Int): CartItemModel {
        return cartItemsList[position]
    }

    fun isEmpty(): Boolean {
        return itemCount == 0
    }

    fun add(r: CartItemModel) {
        cartItemsList.add(r)
        notifyItemInserted(cartItemsList.size - 1)
    }

    fun addLoadingFooter() {
        isLoadingAdded = true
        //add(new OpportunityModel());
        if (!isEmpty()) {
            Log.d("","getItem:addLoadingFooter")
            add(getItem(cartItemsList.size - 1))
            Log.d("", "loading")
        }
    }

    fun removeLoadingFooter() {
        if (isLoadingAdded) {
            val position = cartItemsList.size - 1
            Log.d("","getItem:removeLoadingFooter")
            val result = getItem(position)

            if (result != null) {
                cartItemsList.removeAt(position)
                notifyItemRemoved(position)
            }
        }
        isLoadingAdded = false
    }


    fun addAll(opResults: MutableList<CartItemModel>) {
        for (result in opResults) {
            add(result)
        }
    }




}