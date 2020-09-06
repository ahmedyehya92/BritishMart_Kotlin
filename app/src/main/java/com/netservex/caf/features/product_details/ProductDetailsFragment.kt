package com.netservex.caf.features.product_details

import android.content.Intent
import androidx.lifecycle.Observer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import com.netservex.caf.R
import com.netservex.caf.core.RequestIntervalHandler2
import com.netservex.caf.features.base.BaseFragment
import com.netservex.caf.features.login.LoginFragment
import com.netservex.entities.KEY_NUMBER_OF_ITEMS
import com.netservex.entities.KEY_PLUS_ONE_TO_CART
import com.netservex.entities.ProductModel
import kotlinx.android.synthetic.main.fragment_product_details.*
import kotlinx.android.synthetic.main.view_add_to_cart_fixed_button.*


class ProductDetailsFragment : BaseFragment(), ProductDetailsView {
    // TODO: Rename and change types of parameters
    private var productDetails: ProductModel? = null
    private lateinit var requestIntervalHandler: RequestIntervalHandler2
    private val tryAgainTriggerObserever = Observer<Int> {
        when (it) {
            1 -> productDetails?.id?.let { productId -> presenter?.getProductDetails(productId.toInt()) }
        }
    }

    private val presenter: ProductDetailsPresenter by lazy {
        ProductDetailsImplPresenter(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            productDetails = arguments!!.getSerializable(ARG_PARAM_PRODUCT_DETAILS) as ProductModel
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? { // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requestIntervalHandler =
            RequestIntervalHandler2(lout_loading_interval_view_container, context!!, false)
        requestIntervalHandler.tryAgainTrigger.observe(this, tryAgainTriggerObserever)
        requestIntervalHandler.setMessageErrorTextColor(R.color.colorredMain)

        productDetails?.id?.apply { presenter.getProductDetails(this.toInt()) }




        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        private const val ARG_PARAM_PRODUCT_DETAILS = "ARG_PARAM_PRODUCT_DETAILS"
        fun newInstance( productDetails: ProductModel?): ProductDetailsFragment {
            val fragment = ProductDetailsFragment()
            val args = Bundle()
            args.putSerializable(ARG_PARAM_PRODUCT_DETAILS, productDetails)
            fragment.arguments = args
            return fragment
        }
    }

    override fun addProductDetails(productDetails: ProductModel?) {
        productDetails?.apply {
            Glide.with(context)
                .load(productDetails.image)
                .into(im_product)

            tv_product_name.text = productDetails.name
            Log.e("TAG","price= ${productDetails.price}")
            if(productDetails.oprice!!.isEmpty())
                tv_price.text = productDetails.price
            else
                tv_price.text = productDetails.oprice
            icon_like.isSelected = productDetails.isliked

            val webSettings: WebSettings = tv_description.getSettings()
            webSettings.javaScriptEnabled = true
            webSettings.setLightTouchEnabled(true);
            webSettings.setJavaScriptEnabled(true);
            webSettings.setGeolocationEnabled(true);

            tv_description.loadData(productDetails.intro, "text/html", "UTF-8");

            tv_type.text = productDetails.details

            btn_add_to_cart.setOnClickListener {
                productDetails.id?.apply { presenter.addProductToCart(this.toInt(), 1) }
            }

            icon_like.setOnClickListener {
                productDetails.id?.apply {
                    presenter.favoriteAction(this.toInt())
                }
            }
        }

    }

    override fun successfulAddProductToCart() {
        Toast.makeText(context, "Added to cart", Toast.LENGTH_LONG).show()
        val intent: Intent = Intent("cartChanged")
        intent.putExtra(
            KEY_PLUS_ONE_TO_CART,
            1
        )
        LocalBroadcastManager.getInstance(activity!!).sendBroadcast(intent)
    }

    override fun failedAddProductToCart(message: String?, code: Int?) {
        if(code == 401) {
            Toast.makeText(context, "You must login", Toast.LENGTH_LONG).show()
            activity?.supportFragmentManager?.beginTransaction()?.add(R.id.main_fragment_container, LoginFragment(), "login_fragment")?.addToBackStack("")?.commit()

        }
        else
            Toast.makeText(context, message?:"Unknown message", Toast.LENGTH_LONG).show()
    }

    override fun successfulFavoriteAction(isFavored: Boolean) {
        icon_like.isSelected = isFavored
    }

    override fun failedFavoriteAction(message: String?, code: Int?) {
        if(code == 401) {
            Toast.makeText(context, "You must login", Toast.LENGTH_LONG).show()
            activity?.supportFragmentManager?.beginTransaction()?.add(R.id.main_fragment_container, LoginFragment(), "login_fragment")?.addToBackStack("")?.commit()

        }
        else
            Toast.makeText(context, message?:"Unknown message", Toast.LENGTH_LONG).show()
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