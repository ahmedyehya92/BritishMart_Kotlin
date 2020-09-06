package com.netservex.caf.features.search

import androidx.lifecycle.Observer
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.netservex.caf.R
import com.netservex.caf.core.PaginationAdapterCallBack
import com.netservex.caf.core.PaginationStaggardScrollListener
import com.netservex.caf.core.RequestIntervalHandler2
import com.netservex.caf.features.base.BaseFragment
import com.netservex.caf.features.main.MainActivity
import com.netservex.caf.features.product_details.ProductDetailsFragment
import com.netservex.caf.features.products_list.AdapterProductsList
import com.netservex.entities.ProductModel
import com.netservex.entities.SUBCATEGORY_DATA_TYPE
import kotlinx.android.synthetic.main.fragment_search.*
import java.util.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : BaseFragment(), SearchView, AdapterProductsList.customButtonListener,
    PaginationAdapterCallBack, MainActivity.SearchListener {
    // TODO: Rename and change types of parameters
    private var searchQuery: String? = null

    var adapter: AdapterProductsList? = null
    var staggeredGridLayoutManager: androidx.recyclerview.widget.StaggeredGridLayoutManager? = null
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
            1 -> searchQuery?.let { presenter?.searchProducts(currentPage, it) }
        }
    }
    private val presenter: SearchPresenter by lazy {
        SearchImplPresenter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            searchQuery = getArguments()!!.getString(ARG_PARAM_SEARCH_QUERY)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {





        requestIntervalHandler =
            RequestIntervalHandler2(lout_loading_interval_view_container, context!!, false)
        requestIntervalHandler.tryAgainTrigger.observe(this, tryAgainTriggerObserever)
        requestIntervalHandler.setMessageErrorTextColor(R.color.colorredMain)

        staggeredGridLayoutManager =
            androidx.recyclerview.widget.StaggeredGridLayoutManager(
                2,
                androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL
            )
        products_list.setLayoutManager(staggeredGridLayoutManager)
        products_list.setHasFixedSize(true)
        products_list.setItemViewCacheSize(20)
        products_list.setDrawingCacheEnabled(true)
        products_list.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH)
        populatRecyclerView()
        implementScrolListener()
        currentPage = PAGE_START
        searchQuery?.let { presenter?.searchProducts(currentPage, it) }
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SearchFragment.
         */
        // TODO: Rename and change types and number of parameters
        private const val PAGE_START = 1
        private const val ARG_PARAM_SEARCH_QUERY = "ARG_PARAM_SEARCH_QUERY"


        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun populatRecyclerView() {
        adapter = AdapterProductsList(activity!!, arrayList, SUBCATEGORY_DATA_TYPE)
        adapter!!.setCustomButtonListner(this)
        adapter!!.setPagingAdapterCallback(this)
        products_list.setAdapter(adapter)
        adapter!!.notifyDataSetChanged()
    }


    private fun implementScrolListener() {
        products_list.addOnScrollListener(object :
            PaginationStaggardScrollListener(staggeredGridLayoutManager!!) {
            //protected fun hideCatList() {}


            override fun loadMoreItems() {
                isLoadingV = true
                currentPage += 1
                presenter?.searchProducts(currentPage, searchQuery!!)
            }

            override val totalPageCount: Int = TOTAL_PAGES

            override val isLastPage: Boolean = isLastPageV

            override val isLoading: Boolean = isLoadingV
        })
    }

    override fun addProducts(products: MutableList<ProductModel>) {
        adapter?.addAll(products)
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

    override fun onItemClickListner(productModel: ProductModel) {
        getFragmentManager()?.beginTransaction()?.add(R.id.main_fragment_container, ProductDetailsFragment.newInstance(productModel), "")?.addToBackStack("")?.commit()
    }

    override fun retryPageLoad() {

    }

    override fun onSearchQueryChange(searchQuery: String) {

    }


}
