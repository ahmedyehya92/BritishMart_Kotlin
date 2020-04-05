package com.netservex.caf.features.cart

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.RelativeLayout
import android.widget.TextView
import com.netservex.caf.R
import com.netservex.caf.features.base.BaseFragment
import com.netservex.entities.CartItemModel
import kotlinx.android.synthetic.main.fragment_cart_sec.*
import kotlinx.android.synthetic.main.view_place_order.*
import java.util.*

class CartFragment : BaseFragment(), CartItemsAdapter.CustomeListener {
    /*   @BindView(R.id.btn_product)
    ImageView btnProd;

    @BindView(R.id.btn_product1)
    ImageView btnProd1;

    @BindView(R.id.btn_product2)
    ImageView btnProd2;

    @BindView(R.id.btnPlaceOrder)
    ImageView btnPlaceOrder; */

    var totalPrice: String? = null
    var cartItemsArrayList: MutableList<CartItemModel> =  ArrayList()
    var cartListAdapter: CartItemsAdapter? = null
    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments!!.getString(ARG_PARAM1)
            mParam2 = arguments!!.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View =
            inflater.inflate(R.layout.fragment_cart_sec, container, false)


        return rootView
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        btn_place_order!!.setOnClickListener {
            /*  fragmentManager!!.beginTransaction()
                  .add(R.id.main_fragment_container, AddAddressFragment(), "").addToBackStack("")
                  .commit()*/
        }

        cartItemsArrayList = ArrayList<CartItemModel>()
        cartItemsArrayList!!.add(
            CartItemModel(
                "product",
                1,
                300,
                "https://petersgourmetmarket.com/wp-content/uploads/2015/07/DSC_2595.jpg",
                "2"
            )
        )
        cartItemsArrayList!!.add(
            CartItemModel(
                "product",
                1,
                10,
                "https://static.shoplightspeed.com/shops/618750/files/008894922/image.jpg",
                "2"
            )
        )
        cartItemsArrayList!!.add(
            CartItemModel(
                "product",
                1,
                30,
                "https://static.shoplightspeed.com/shops/618750/files/008898824/image.jpg",
                "2"
            )
        )
        cartItemsArrayList!!.add(
            CartItemModel(
                "product",
                1,
                5,
                "https://static.shoplightspeed.com/shops/618750/files/008898632/image.jpg",
                "2"
            )
        )
        cartItemsArrayList!!.add(
            CartItemModel(
                "product",
                1,
                10,
                "https://static.shoplightspeed.com/shops/618750/files/010297995/image.jpg",
                "2"
            )
        )
        cartItemsArrayList!!.add(
            CartItemModel(
                "product",
                1,
                20,
                "https://petersgourmetmarket.com/wp-content/uploads/2015/07/DSC_2595.jpg",
                "2"
            )
        )
        cartItemsArrayList!!.add(
            CartItemModel(
                "product",
                1,
                8,
                "https://petersgourmetmarket.com/wp-content/uploads/2015/07/DSC_2595.jpg",
                "2"
            )
        )
        cartListAdapter = CartItemsAdapter(cartItemsArrayList,context)
        cartListAdapter?.setCustomButtonListner(this)
        lv_cart_list!!.adapter = cartListAdapter
        updateTotalPrice()
    }

    override fun onRemoveButtonClickListner(productId: String?, position: Int) {
        cartListAdapter?.removeAtPosition(position)
        cartListAdapter?.notifyItemRemoved(position)
        cartListAdapter?.let {
            if(it.isEmpty())
            {
                //showEmptyViewForList()
            }

        }
    }
    override fun onAmountEditListener(currentQuantity: Int?, position: Int) {
        Log.d("", "onAmountEditListener: $currentQuantity")

        cartListAdapter?.getItem(position)?.quantity = currentQuantity
        cartListAdapter?.notifyItemChanged(position)

        updateTotalPrice()
    }

    override fun onItemClickListener(productId: String?) {
     /*   fragmentManager!!.beginTransaction()
            .add(R.id.main_fragment_container, ProductDetailsFragment(), "").addToBackStack("")
            .commit()*/
    }

    fun updateTotalPrice() {
        var totalPrice = 0
        for (i in cartItemsArrayList!!.indices) {
            cartItemsArrayList!![i].priceForTotalQuantity?.let { totalPrice += it }
        }
        tv_total_price!!.text = totalPrice.toString()
    }

    fun getViewByPosition(pos: Int, listView: ListView?): View {
        val firstListItemPosition = listView!!.firstVisiblePosition
        val lastListItemPosition = firstListItemPosition + listView.childCount - 1
        return if (pos < firstListItemPosition || pos > lastListItemPosition) {
            listView.adapter.getView(pos, null, listView)
        } else {
            val childIndex = pos - firstListItemPosition
            listView.getChildAt(childIndex)
        }
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CartFragment.
         */
// TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?): CartFragment {
            val fragment = CartFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}
