package com.netservex.caf.features.offers

import androidx.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.netservex.caf.R
import com.netservex.caf.core.PaginationAdapterCallBack
import com.netservex.caf.core.PaginationStaggardScrollListener
import com.netservex.caf.core.RequestIntervalHandler2
import com.netservex.caf.features.base.BaseFragment
import com.netservex.caf.features.product_details.ProductDetailsFragment
import com.netservex.entities.OfferModel
import com.netservex.entities.ProductModel
import com.netservex.entities.SUBCATEGORY_DATA_TYPE
import kotlinx.android.synthetic.main.fragment_offers.*
import java.util.ArrayList

class OffersListFragment : BaseFragment(), OffersListView,
    OffersListAdapter.customButtonListener, PaginationAdapterCallBack {
    // TODO: Rename and change types of parameters
    private var subCategoryId: String? = null
    var adapter: OffersListAdapter? = null
    private var isLoadingV: Boolean = false
    private var isLastPageV: Boolean = false
    private var TOTAL_PAGES: Int = 50
    private lateinit var requestIntervalHandler: RequestIntervalHandler2
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    val totalPageCount = 20
    private var currentPage = PAGE_START
    private var arrayList: ArrayList<ProductModel>? = null
    var handler: Handler? = null
    private val tryAgainTriggerObserever = Observer<Int> {
        when (it) {
            1 -> presenter?.getOffersList(currentPage)
        }
    }
    private val presenter: OffersListPresenter by lazy {
        OffersListImplPresenter(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (getArguments() != null) {
            subCategoryId = getArguments()!!.getString(ARG_PARAM_SUB_CATEGORY_ID)
        }
        handler = Handler(Looper.getMainLooper())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View { // Inflate the layout for this fragment
        val rootView: View =
            inflater.inflate(R.layout.fragment_offers, container, false)

        arrayList = ArrayList<ProductModel>()
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        requestIntervalHandler =
            RequestIntervalHandler2(lout_loading_interval_view_container, context!!, false)
        requestIntervalHandler.tryAgainTrigger.observe(this, tryAgainTriggerObserever)
        requestIntervalHandler.setMessageErrorTextColor(R.color.colorredMain)

        offers_list.setHasFixedSize(true)
        offers_list.setItemViewCacheSize(20)
        offers_list.setDrawingCacheEnabled(true)
        offers_list.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH)
        populatRecyclerView()
        implementScrolListener()
        currentPage = PAGE_START
        presenter?.getOffersList(currentPage)
        super.onViewCreated(view, savedInstanceState)
    }

    private fun populatRecyclerView() {
        adapter = OffersListAdapter(activity!!, arrayList, SUBCATEGORY_DATA_TYPE)
        adapter!!.setCustomButtonListner(this)
        adapter!!.setPagingAdapterCallback(this)
        offers_list.setAdapter(adapter)
        adapter!!.notifyDataSetChanged()
    }

    private fun implementScrolListener() {
        offers_list.addOnScrollListener(object :
            PaginationStaggardScrollListener(offers_list.layoutManager as androidx.recyclerview.widget.StaggeredGridLayoutManager) {
            //protected fun hideCatList() {}


            override fun loadMoreItems() {
                isLoadingV = true
                currentPage += 1
                presenter?.getOffersList(currentPage)
            }

            override val totalPageCount: Int = TOTAL_PAGES

            override val isLastPage: Boolean = isLastPageV

            override val isLoading: Boolean = isLoadingV
        })
    }

    override fun addOffers(offers: MutableList<ProductModel>) {
        adapter!!.addAll(offers)
    }

    fun passContext(): Context {
        return activity!!
    }

    override fun setLastPageTrue() {
        isLastPageV = true
    }

    override fun addLoadingFooter() {
        adapter?.addLoadingFooter()
    }

    override fun removeLoadingFooter() {
        adapter?.removeLoadingFooter()
    }

    override fun showRetryAdapter() {
        adapter?.showRetry(true,"Error")
    }

    override fun setIsLoadingFalse() {
        isLoadingV = false
    }

    override fun showEmptyViewForList() {
        finishLoading()
        //view_empty_list.visibility = View.VISIBLE
    }

    override fun showLoading() {
        requestIntervalHandler.showLoadingView(null)
    }

    override fun finishLoading() {
        requestIntervalHandler.finishLoading()
    }

    override fun connectionError(message: String?) {
        requestIntervalHandler.showErrorView("error connection, try again!")
    }
    override fun faildLoading(message: Any) {
    }

    override fun onItemClickListner(offer: ProductModel) {
        getFragmentManager()?.beginTransaction()?.add(R.id.main_fragment_container, ProductDetailsFragment.newInstance(offer), "")?.addToBackStack("")?.commit()
    }

    override fun retryPageLoad() {
        presenter?.getOffersList(currentPage)
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM_SUB_CATEGORY_ID = "ARG_PARAM_CATEGORY_ID"
        private const val PAGE_START = 1
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param subCategoryId Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SubCategoriesFragment.
         */
// TODO: Rename and change types and number of parameters
        fun newInstance(subCategoryId: String?): OffersListFragment {
            val fragment = OffersListFragment()
            val args = Bundle()
            args.putString(ARG_PARAM_SUB_CATEGORY_ID, subCategoryId)
            fragment.setArguments(args)
            return fragment
        }
    }
}