package com.netservex.caf.features.cart

import android.content.Intent
import androidx.lifecycle.Observer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.netservex.caf.R
import com.netservex.caf.core.RequestIntervalHandler2
import com.netservex.caf.features.base.BaseFragment
import com.netservex.caf.features.checkout.CheckoutFragment
import com.netservex.entities.CartItemModel
import com.netservex.entities.KEY_NUMBER_OF_ITEMS
import kotlinx.android.synthetic.main.fragment_cart_sec.*
import kotlinx.android.synthetic.main.view_place_order.*

class CartFragment : BaseFragment(), CartItemsAdapter.CustomeListener, CartView {
    /*   @BindView(R.id.btn_product)
    ImageView btnProd;

    @BindView(R.id.btn_product1)
    ImageView btnProd1;

    @BindView(R.id.btn_product2)
    ImageView btnProd2;

    @BindView(R.id.btnPlaceOrder)
    ImageView btnPlaceOrder; */

    private lateinit var requestIntervalHandler: RequestIntervalHandler2
    private val tryAgainTriggerObserever = Observer<Int> {
        when (it) {
            1 -> presenter.getCartItems()
        }
    }

    private val presenter: CartPresenter by lazy {
        CartImplPresenter(this)
    }

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

        requestIntervalHandler =
            RequestIntervalHandler2(lout_loading_interval_view_container, context!!, false)
        requestIntervalHandler.tryAgainTrigger.observe(this, tryAgainTriggerObserever)
        requestIntervalHandler.setMessageErrorTextColor(R.color.colorredMain)


        btn_place_order!!.setOnClickListener {
              fragmentManager!!.beginTransaction()
                  .add(R.id.main_fragment_container, CheckoutFragment(), "").addToBackStack("")
                  .commit()

        }

        presenter.getCartItems()

   /*     cartItemsArrayList = ArrayList<CartItemModel>()
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
        updateTotalPrice()*/
    }

    override fun onRemoveButtonClickListner(productId: String?, position: Int) {
        presenter.removeProductFromCart(position, productId!!.toInt())
    }
    override fun onAmountEditListener(currentQuantity: Int, position: Int) {
        Log.d("", "onAmountEditListener: $currentQuantity")
        Log.d("OnAmountEdit","currentQuantity = $currentQuantity")
        presenter.editProductAmount(position, cartListAdapter!!.getItem(position).id!!.toInt(), currentQuantity)

        //updateTotalPrice()
    }

    override fun onItemClickListener(productId: String?) {
     /*   fragmentManager!!.beginTransaction()
            .add(R.id.main_fragment_container, ProductDetailsFragment(), "").addToBackStack("")
            .commit()*/
    }

    fun updateTotalPrice(cartItemsArrayList: MutableList<CartItemModel>) {
        var totalPrice = 0.0
        cartItemsArrayList.forEach {
            Log.e("cartItemsArrayList", "total = ${it.quantity?.times(it.priceOfUnit)}")
            totalPrice +=  it.quantity?.times(it.priceOfUnit)
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

    override fun addToItems(requests: MutableList<CartItemModel>) {
        cartListAdapter = CartItemsAdapter(requests,context)
        cartListAdapter?.setCustomButtonListner(this)
        lv_cart_list!!.adapter = cartListAdapter
        updateTotalPrice(cartListAdapter!!.getItems())
    }

    override fun successfulEditAmount(position: Int, quantity: Int) {
        cartListAdapter?.getItem(position)?.quantity = quantity
        cartListAdapter?.notifyItemChanged(position)
        updateTotalPrice(cartListAdapter!!.getItems())
    }

    override fun failedEditAmount(message: String?) {
        Toast.makeText(context,"Connection error, Try Again!", Toast.LENGTH_LONG).show()
    }

    override fun successfulRemoveProduct(position: Int) {
        val intent: Intent = Intent("cartChanged")
        intent.putExtra(
            KEY_NUMBER_OF_ITEMS,
            cartListAdapter!!.itemCount-1
        )
        LocalBroadcastManager.getInstance(activity!!).sendBroadcast(intent)

        cartListAdapter?.removeAtPosition(position)
        cartListAdapter?.notifyItemRemoved(position)
        cartListAdapter?.let {
            if(it.isEmpty())
            {
                //showEmptyViewForList()
            }

        }
        updateTotalPrice(cartListAdapter!!.getItems())
    }

    override fun failedRemoveProduct(message: String?) {
        Toast.makeText(context,"Connection error, Try Again!", Toast.LENGTH_LONG).show()
    }

    override fun showLoading() {
        requestIntervalHandler.showLoadingView()
    }

    override fun finishLoading() {
        requestIntervalHandler.finishLoading()
    }

    override fun connectionError(message: String?) {
        requestIntervalHandler.showErrorView(message)
    }

    override fun faildLoading(message: Any) {
    }
}
