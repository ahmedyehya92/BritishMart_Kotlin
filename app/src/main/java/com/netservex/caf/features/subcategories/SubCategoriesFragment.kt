package com.netservex.caf.features.subcategories

import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.netservex.caf.R
import com.netservex.caf.core.PaginationAdapterCallBack
import com.netservex.caf.core.PaginationStaggardScrollListener
import com.netservex.caf.core.RequestIntervalHandler2
import com.netservex.caf.features.base.BaseFragment
import com.netservex.caf.features.products_list.FragmentProductsList
import com.netservex.entities.CategoryModel
import com.netservex.entities.SUBCATEGORY_DATA_TYPE
import kotlinx.android.synthetic.main.fragment_sub_categories.*

class SubCategoriesFragment : BaseFragment(), SubCategoriesView,
    SubcategoriesAdapter.customButtonListener, PaginationAdapterCallBack {
    // TODO: Rename and change types of parameters
    private var categoryId: String? = null
    var adapter: SubcategoriesAdapter? = null
    var staggeredGridLayoutManager: StaggeredGridLayoutManager? = null
    private var isLoadingV: Boolean = false
    private var isLastPageV: Boolean = false
    private var TOTAL_PAGES: Int = 50
    var linearLayoutManager: LinearLayoutManager? = null
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    val totalPageCount = 20
    private var currentPage = PAGE_START
    private var arrayList: ArrayList<CategoryModel>? = null
    var handler: Handler? = null
    private lateinit var requestIntervalHandler: RequestIntervalHandler2


    private val tryAgainTriggerObserever = Observer<Int> {
        when (it) {
            1 -> categoryId?.let { presenter?.getSubCategories(currentPage, it) }
        }
    }

    private val presenter: SubCategoriesPresenter by lazy {
        SubCategoriesImplPresenter(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (getArguments() != null) {
            categoryId = getArguments()!!.getString(ARG_PARAM_CATEGORY_ID)
        }
        handler = Handler(Looper.getMainLooper())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View { // Inflate the layout for this fragment
        val rootView: View =
            inflater.inflate(R.layout.fragment_sub_categories, container, false)

        arrayList = ArrayList<CategoryModel>()
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        requestIntervalHandler =
            RequestIntervalHandler2(lout_loading_interval_view_container, context!!, false)
        requestIntervalHandler.tryAgainTrigger.observe(this, tryAgainTriggerObserever)
        requestIntervalHandler.setMessageErrorTextColor(R.color.colorredMain)


        linearLayoutManager = LinearLayoutManager(getContext())
        staggeredGridLayoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        im_list_icon.setOnClickListener {
            staggeredGridLayoutManager =
                StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            sub_cat_home_list.setLayoutManager(staggeredGridLayoutManager)
            //adapter!!.notifyDataSetChanged()
            im_list_icon.setImageResource(R.drawable.icon_list_selected)
            im_grid_icon.setImageResource(R.drawable.icon_grid_normal)
        }
        im_grid_icon.setOnClickListener {
            staggeredGridLayoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            sub_cat_home_list.setLayoutManager(staggeredGridLayoutManager)
            //adapter!!.notifyDataSetChanged()
            im_grid_icon.setImageResource(R.drawable.icon_grid_selected)
            im_list_icon.setImageResource(R.drawable.icon_list_normal)
        }
        sub_cat_home_list.setLayoutManager(staggeredGridLayoutManager)
        sub_cat_home_list.setHasFixedSize(true)
        sub_cat_home_list.setItemViewCacheSize(20)
        sub_cat_home_list.setDrawingCacheEnabled(true)
        sub_cat_home_list.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH)
        populatRecyclerView()
        implementScrolListener()
        currentPage = PAGE_START
        categoryId?.let { presenter?.getSubCategories(currentPage, it) }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun populatRecyclerView() {
        adapter = SubcategoriesAdapter(activity!!, arrayList,SUBCATEGORY_DATA_TYPE)
        adapter!!.setCustomButtonListner(this)
        adapter!!.setPagingAdapterCallback(this)
        sub_cat_home_list.setAdapter(adapter)
        adapter!!.notifyDataSetChanged()
    }

    private fun implementScrolListener() {
        sub_cat_home_list.addOnScrollListener(object :
            PaginationStaggardScrollListener(staggeredGridLayoutManager!!) {
            //protected fun hideCatList() {}


            override fun loadMoreItems() {
                isLoadingV = true
                currentPage += 1
                presenter?.getSubCategories(currentPage, categoryId!!)
            }

            override val totalPageCount: Int = TOTAL_PAGES

            override val isLastPage: Boolean = isLastPageV

            override val isLoading: Boolean = isLoadingV
        })
    }

    override fun addSubCategories(subCategories: MutableList<CategoryModel>) {
        adapter!!.addAll(subCategories)
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

    override fun connectionError() {
        requestIntervalHandler.showErrorView("error connection, try again!")
    }

    override fun faildLoading(message: Any) {
    }

    override fun onItemClickListner(id: String?, title: String?) {
        getFragmentManager()?.beginTransaction()?.add(R.id.main_fragment_container, FragmentProductsList.newInstance(id), "")?.addToBackStack("")?.commit()
    }

    override fun retryPageLoad() {
        presenter?.getSubCategories(currentPage, categoryId!!)
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM_CATEGORY_ID = "ARG_PARAM_CATEGORY_ID"
        private const val PAGE_START = 1
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SubCategoriesFragment.
         */
// TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?): SubCategoriesFragment {
            val fragment = SubCategoriesFragment()
            val args = Bundle()
            args.putString(ARG_PARAM_CATEGORY_ID, param1)
            fragment.setArguments(args)
            return fragment
        }
    }
}
