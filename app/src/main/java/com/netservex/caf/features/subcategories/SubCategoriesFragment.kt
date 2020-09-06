package com.netservex.caf.features.subcategories

import androidx.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.netservex.caf.R
import com.netservex.caf.core.PaginationAdapterCallBack
import com.netservex.caf.core.PaginationStaggardScrollListener
import com.netservex.caf.core.RequestIntervalHandler2
import com.netservex.caf.features.base.BaseFragment
import com.netservex.caf.features.products_list.FragmentProductsList
import com.netservex.entities.SUBCATEGORY_DATA_TYPE
import com.netservex.entities.SubCategoryModel
import kotlinx.android.synthetic.main.fragment_sub_categories.*

class SubCategoriesFragment : BaseFragment(), SubCategoriesView,
    SubcategoriesAdapter.customButtonListener, PaginationAdapterCallBack {
    // TODO: Rename and change types of parameters
    private var categoryId: String? = null
    private var categoryName: String? = null
    var adapter: SubcategoriesAdapter? = null
    var staggeredGridLayoutManager: androidx.recyclerview.widget.StaggeredGridLayoutManager? = null
    private var isLoadingV: Boolean = false
    private var isLastPageV: Boolean = false
    private var TOTAL_PAGES: Int = 50
    private val TAG = this::class.java.simpleName
    var linearLayoutManager: androidx.recyclerview.widget.LinearLayoutManager? = null
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    val totalPageCount = 20
    private var currentPage = PAGE_START
    private var arrayList: ArrayList<SubCategoryModel>? = null
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
            categoryName = arguments?.getString(ARG_PARAM_CATEGORY_NAME)
        }
        handler = Handler(Looper.getMainLooper())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View { // Inflate the layout for this fragment
        val rootView: View =
            inflater.inflate(R.layout.fragment_sub_categories, container, false)

        arrayList = ArrayList()
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        requestIntervalHandler =
            RequestIntervalHandler2(lout_loading_interval_view_container, context!!, false)
        requestIntervalHandler.tryAgainTrigger.observe(this, tryAgainTriggerObserever)
        requestIntervalHandler.setMessageErrorTextColor(R.color.colorredMain)

        tv_category_name.text = categoryName
        Log.e("cat", "category name = $categoryName")

        linearLayoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(getContext())
        staggeredGridLayoutManager =
            androidx.recyclerview.widget.StaggeredGridLayoutManager(
                2,
                androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL
            )
        im_list_icon.setOnClickListener {
            staggeredGridLayoutManager =
                androidx.recyclerview.widget.StaggeredGridLayoutManager(
                    1,
                    androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL
                )
            sub_cat_home_list.setLayoutManager(staggeredGridLayoutManager)
            //adapter!!.notifyDataSetChanged()
            im_list_icon.setImageResource(R.drawable.icon_list_selected)
            im_grid_icon.setImageResource(R.drawable.icon_grid_normal)
        }
        im_grid_icon.setOnClickListener {
            staggeredGridLayoutManager =
                androidx.recyclerview.widget.StaggeredGridLayoutManager(
                    2,
                    androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL
                )
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
        categoryId?.let {
            Log.d(TAG, "category id = $categoryId")
            presenter?.getSubCategories(currentPage, it) }
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

    override fun addSubCategories(subCategories: MutableList<SubCategoryModel>) {
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

    override fun connectionError(message: String?) {
        requestIntervalHandler.showErrorView("error connection, try again!")
    }

    override fun faildLoading(message: Any) {
    }

    override fun onItemClickListner(id: String?, title: String?) {
        getFragmentManager()?.beginTransaction()?.add(R.id.main_fragment_container, FragmentProductsList.newInstance(id, title                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          ), "title")?.addToBackStack("")?.commit()
    }

    override fun retryPageLoad() {
        presenter?.getSubCategories(currentPage, categoryId!!)
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM_CATEGORY_ID = "ARG_PARAM_CATEGORY_ID"
        private const val ARG_PARAM_CATEGORY_NAME = "ARG_PARAM_CATEGORY_NAME"
        private const val PAGE_START = 1
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param categoryId Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SubCategoriesFragment.
         */
// TODO: Rename and change types and number of parameters
        fun newInstance(categoryId: String?, categoryName: String?): SubCategoriesFragment {
            val fragment = SubCategoriesFragment()
            val args = Bundle()
            args.putString(ARG_PARAM_CATEGORY_ID, categoryId)
            args.putString(ARG_PARAM_CATEGORY_NAME, categoryName)
            fragment.setArguments(args)
            return fragment
        }
    }

    override fun onDestroy() {
        presenter.onDestroy(this)
        super.onDestroy()
    }
}
