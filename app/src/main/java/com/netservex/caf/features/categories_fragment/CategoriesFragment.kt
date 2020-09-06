package com.netservex.caf.features.categories_fragment

import androidx.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.netservex.caf.R
import com.netservex.caf.core.PassedDataFromCategoriesToMenu
import com.netservex.caf.core.RequestIntervalHandler2
import com.netservex.caf.core.RxBus
import com.netservex.caf.customviews.CustomRecyclerView
import com.netservex.caf.features.base.BaseFragment
import com.netservex.caf.features.subcategories.SubCategoriesFragment
import com.netservex.entities.CategoryModel
import kotlinx.android.synthetic.main.fragment_categories.*

class CategoriesFragment : BaseFragment(), CategoriesView,
    AdapterCategories.CustomButtonListener {

    private val presenter: CategoriesPresenter by lazy {
        CategoriesImplPresenter(this)
    }

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    private val tryAgainTriggerObserever = Observer<Int> {
        when (it) {
            1 -> presenter.loadCategories()
        }
    }
    private lateinit var requestIntervalHandler: RequestIntervalHandler2
    private var newsAdapter: AdapterCategories? = null
    var staggeredGridLayoutManager: androidx.recyclerview.widget.StaggeredGridLayoutManager? = null

    private var arrayList: ArrayList<CategoryModel>? = null
    private var mHandler: Handler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (getArguments() != null) {
            mParam1 = getArguments()!!.getString(ARG_PARAM1)
            mParam2 = getArguments()!!.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View { // Inflate the layout for this fragment
        mHandler = Handler(Looper.getMainLooper())
        val view: View =
            inflater.inflate(R.layout.fragment_categories, container, false)
        arrayList = ArrayList<CategoryModel>()
        Log.d("", "onCreateView: ")
        return view
    }

    override fun onResume() {
        super.onResume()
        //presenter.loadCategories();
//Toast.makeText(getActivity(), String.valueOf(arrayList.size()), Toast.LENGTH_SHORT).show();
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        requestIntervalHandler =
            RequestIntervalHandler2(lout_loading_interval_view_container, context!!, false)
        requestIntervalHandler.tryAgainTrigger.observe(this, tryAgainTriggerObserever)
        requestIntervalHandler.setMessageErrorTextColor(R.color.colorredMain)

        staggeredGridLayoutManager =
            androidx.recyclerview.widget.StaggeredGridLayoutManager(
                3,
                androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL
            )
        cat_home_list.setLayoutManager(staggeredGridLayoutManager)
        cat_home_list.setHasFixedSize(true)
        Log.d("", "onViewCreated: ")
        populatRecyclerView()
        presenter.loadCategories()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun populatRecyclerView() {
        newsAdapter = AdapterCategories(getActivity()!!, arrayList!!)
        newsAdapter?.setCustomButtonListner(this)
        cat_home_list?.setAdapter(newsAdapter)
        newsAdapter?.notifyDataSetChanged()
    }

    override fun onItemCategoryClickListner(title: String?, id: String?) {
        activity?.getSupportFragmentManager()?.beginTransaction()?.add(R.id.main_fragment_container, SubCategoriesFragment.newInstance(id, title), "")?.addToBackStack(null)?.commit()
    }

    override fun showLoading() {
        requestIntervalHandler.showLoadingView()
    }

    override fun finishLoading() {
        requestIntervalHandler.finishLoading()
    }

    override fun connectionError(message: String?) {
        requestIntervalHandler.showErrorView(message?:"Unknown error")
    }

    override fun addMoreToAdapter(list: ArrayList<CategoryModel>) {
        RxBus.publish(PassedDataFromCategoriesToMenu(list))
        mHandler!!.post { newsAdapter?.addAll(list) }
    }

    override fun passContext(): Context {
        return context!!
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
         * @return A new instance of fragment CategoriesFragment.
         */
// TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?): CategoriesFragment {
            val fragment = CategoriesFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.setArguments(args)
            return fragment
        }
    }

    override fun onDestroy() {
        presenter.onDestroy(this)
        super.onDestroy()
    }
}