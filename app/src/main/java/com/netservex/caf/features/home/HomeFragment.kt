package com.netservex.caf.features.home

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import com.netservex.caf.R
import com.netservex.caf.core.*
import com.netservex.caf.features.categories_fragment.AdapterCategories
import com.netservex.caf.features.offers.OffersListFragment
import com.netservex.caf.features.product_details.ProductDetailsFragment
import com.netservex.caf.features.subcategories.SubCategoriesFragment
import com.netservex.entities.CategoryModel
import com.netservex.entities.KEY_NUMBER_OF_ITEMS
import com.netservex.entities.ProductModel
import com.netservex.entities.SUBCATEGORY_DATA_TYPE
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : androidx.fragment.app.Fragment(),
    HomeView,
    FeaturedProductsAdapter.customButtonListener,
    PaginationAdapterCallBack,
        AdapterCategories.CustomButtonListener
{
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var adapter: FeaturedProductsAdapter? = null
    var staggeredFeaturedProductsGridLayoutManager: androidx.recyclerview.widget.StaggeredGridLayoutManager? = null
    private var isLoadingV: Boolean = false
    private var isLastPageV: Boolean = false
    private var TOTAL_PAGES: Int = 50
    var countDownTimer: CountDownTimer? =null
    var linearLayoutManager: androidx.recyclerview.widget.LinearLayoutManager? = null
    private var categoriesAdapter: AdapterCategories? = null
    private lateinit var requestIntervalHandlerCategories: RequestIntervalHandler2
    private lateinit var requestIntervalHandlerFeaturedProducts: RequestIntervalHandler2
    private lateinit var requestIntervalHandlerMainOffer: RequestIntervalHandler2
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    val totalPageCount = 20
    private var currentPage = PAGE_START
    private var arrayList: ArrayList<ProductModel>? = null
    private var categoriesList: ArrayList<CategoryModel>? = null
    private val tryAgainTriggerObsereverFeaturedProducts = Observer<Int> {
        when (it) {
            1 -> presenter?.getFeaturedProductsList(currentPage)
        }
    }

    private val tryAgainTriggerObsereverCategories = Observer<Int> {
        when (it) {
            1 -> presenter?.loadCategories()
        }
    }

    private val tryAgainTriggerObsereverMainOffer = Observer<Int> {
        when (it) {
            1 -> presenter?.loadMainOffer()
        }
    }

    private val presenter: HomePresenter by lazy {
        HomeImplPresenter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requestIntervalHandlerFeaturedProducts =
            RequestIntervalHandler2(lout_loading_interval_view_container_featured_products, context!!, false)
        requestIntervalHandlerFeaturedProducts.tryAgainTrigger.observe(this, tryAgainTriggerObsereverFeaturedProducts)
        requestIntervalHandlerFeaturedProducts.setMessageErrorTextColor(R.color.colorredMain)

        requestIntervalHandlerCategories =
            RequestIntervalHandler2(lout_loading_interval_view_container_categories, context!!, false)
        requestIntervalHandlerCategories.tryAgainTrigger.observe(this, tryAgainTriggerObsereverCategories)
        requestIntervalHandlerCategories.setMessageErrorTextColor(R.color.colorredMain)

        requestIntervalHandlerMainOffer =
            RequestIntervalHandler2(lout_loading_interval_view_container_offer, context!!, false)
        requestIntervalHandlerMainOffer.tryAgainTrigger.observe(this, tryAgainTriggerObsereverMainOffer)
        requestIntervalHandlerMainOffer.setMessageErrorTextColor(R.color.colorredMain)

        staggeredFeaturedProductsGridLayoutManager =
            androidx.recyclerview.widget.StaggeredGridLayoutManager(
                2,
                androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL
            )

        btn_show_more.setOnClickListener {
            activity?.getSupportFragmentManager()?.beginTransaction()?.add(
                R.id.main_fragment_container,
                OffersListFragment(),
                "offers_fragment"
            )?.addToBackStack(null)?.commit()
        }

        featured_products_list.setLayoutManager(staggeredFeaturedProductsGridLayoutManager)
        featured_products_list.setHasFixedSize(true)
        featured_products_list.setItemViewCacheSize(20)
        featured_products_list.setDrawingCacheEnabled(true)
        featured_products_list.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH)
        populatCategoriesRecyclerView()
        populatRecyclerViewFeaturedProducts()
        //implementScrolListenerFeaturedProducts()
        currentPage = PAGE_START
        presenter.loadCategories()
        presenter.loadMainOffer()
        presenter.getFeaturedProductsList(currentPage)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters

        private const val PAGE_START = 1

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun populatRecyclerViewFeaturedProducts() {
        arrayList = ArrayList()
        adapter = FeaturedProductsAdapter(activity!!, arrayList, SUBCATEGORY_DATA_TYPE)
        adapter!!.setCustomButtonListner(this)
        adapter!!.setPagingAdapterCallback(this)
        featured_products_list.setAdapter(adapter)
        adapter!!.notifyDataSetChanged()
    }

    private fun implementScrolListenerFeaturedProducts() {
        featured_products_list.addOnScrollListener(object :
            PaginationStaggardScrollListener(staggeredFeaturedProductsGridLayoutManager!!) {
            //protected fun hideCatList() {}


            override fun loadMoreItems() {
                isLoadingV = true
                currentPage += 1
                presenter?.getFeaturedProductsList(currentPage)
            }

            override val totalPageCount: Int = TOTAL_PAGES

            override val isLastPage: Boolean = isLastPageV

            override val isLoading: Boolean = isLoadingV
        })
    }

    override fun addFeaturedProducts(products: MutableList<ProductModel>) {
        adapter!!.addAll(products)
    }

    override fun setFeaturedProductsLastPageTrue() {
        isLastPageV = true
    }

    override fun addFeaturedProductsLoadingFooter() {
        adapter?.addLoadingFooter()
    }

    override fun removeFeaturedProductsLoadingFooter() {
        adapter?.removeLoadingFooter()

    }

    override fun showFeaturedProductsRetryAdapter() {
        adapter?.showRetry(true,"Error")
    }

    override fun setFeaturedProductsIsLoadingFalse() {
        isLoadingV = false
    }

    override fun showFeaturedProductsEmptyViewForList() {
        finishFeaturedProductsLoading()
    }

    override fun showFeaturedProductsLoading() {
        requestIntervalHandlerFeaturedProducts.showLoadingView(null)

    }

    override fun finishFeaturedProductsLoading() {
        requestIntervalHandlerFeaturedProducts.finishLoading()

    }

    override fun connectionFeaturedProductsError(message: String?) {
        requestIntervalHandlerFeaturedProducts.showErrorView("error connection, try again!")
    }

    override fun failedFeaturedProductsLoading(message: Any) {
    }

    override fun numberOfCartItems(cartItems: Int) {
        val intent: Intent = Intent("cartChanged")
        intent.putExtra(
            KEY_NUMBER_OF_ITEMS,
            cartItems
        )
        LocalBroadcastManager.getInstance(activity!!).sendBroadcast(intent)
    }


    override fun onItemFeaturedClickListner(productModel: ProductModel) {
        getFragmentManager()?.beginTransaction()?.add(R.id.main_fragment_container, ProductDetailsFragment.newInstance(productModel), "")?.addToBackStack("")?.commit()
    }

    override fun retryPageLoad() {

    }



    private fun populatCategoriesRecyclerView() {
        categoriesList = ArrayList()
        categoriesAdapter = AdapterCategories(getActivity()!!, categoriesList!!)
        categoriesAdapter?.setCustomButtonListner(this)
        cat_home_list?.setAdapter(categoriesAdapter)
        categoriesAdapter?.notifyDataSetChanged()
    }

    override fun onItemCategoryClickListner(title: String?, id: String?) {
        activity?.getSupportFragmentManager()?.beginTransaction()?.add(R.id.main_fragment_container, SubCategoriesFragment.newInstance(id, title), "")?.addToBackStack(null)?.commit()
    }


    override fun showCategoriesLoading() {
        requestIntervalHandlerCategories.showLoadingView()
    }

    override fun finishCategoriesLoading() {
        requestIntervalHandlerCategories.finishLoading()
    }

    override fun connectionCategoriesError(message: String?) {
        requestIntervalHandlerCategories.showErrorView(message?:"Unknown error")
    }

    override fun addMoreCategoriesToAdapter(list: ArrayList<CategoryModel>) {
        RxBus.publish(PassedDataFromCategoriesToMenu(list))
        categoriesAdapter?.addAll(list)
    }







    override fun showOfferLoading() {
        requestIntervalHandlerMainOffer.showLoadingView()

    }

    override fun finishOfferLoading() {
        requestIntervalHandlerMainOffer.finishLoading()

    }

    override fun connectionOfferError(message: String?) {
        requestIntervalHandlerMainOffer.showErrorView(message?:"Unknown error")
    }

    override fun addOfferDetails(offer: ProductModel?) {
        offer?.apply {
            Glide.with(context)
                .load(offer.image)
                .into(im_offer)
            tv_offer_name.text = offer.name
            tv_price.text = "€${offer.price}"
            tv_price.paintFlags = tv_offer_price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            tv_offer_price.text= "€${offer.oprice}"
            tv_discount_percentage.text = "(- ${offer.discount_percentage})"
            tv_description.text = offer.intro
            btn_show_offer_details.setOnClickListener {
                getFragmentManager()?.beginTransaction()?.add(R.id.main_fragment_container, ProductDetailsFragment.newInstance(offer), "")?.addToBackStack("")?.commit()
            }
            startCountdown((this.offer_end_timestamp!!.toLong()*1000)-(Calendar.getInstance().timeInMillis))
        }
    }

    private fun startCountdown(timeInMelliSeconds: Long) {
        countDownTimer = object : CountDownTimer(timeInMelliSeconds!!, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                var n = millisUntilFinished / 1000

                val days = n / (24 * 3600)
                n = n % (24 * 3600)

                val hours = n / 3600
                n %= 3600;
                val minutes = n / 60

                n %= 60

                val seconds = n


                tv_day_offer.text = "%02d".format(days)
                tv_hours_offer.text = "%02d".format(hours)
                tv_offer_minutes.text = "%02d".format(minutes)
                tv_offer_seconds.text = "%02d".format(seconds)
            }

            override fun onFinish() {

            }
        }.start()
    }


    override fun onDestroy() {
        presenter.onDestroy(this)
        countDownTimer?.cancel()
        super.onDestroy()
    }
}
