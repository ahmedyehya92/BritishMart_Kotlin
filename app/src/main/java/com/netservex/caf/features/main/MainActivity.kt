package com.netservex.caf.features.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.core.view.MenuItemCompat
import androidx.appcompat.app.ActionBar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.jakewharton.rxbinding2.widget.RxTextView
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent
import com.netservex.caf.R
import com.netservex.caf.core.*
import com.netservex.caf.features.base.BaseActivity
import com.netservex.caf.features.cart.CartFragment
import com.netservex.caf.features.categories_fragment.CategoriesFragment
import com.netservex.caf.features.home.HomeFragment
import com.netservex.caf.features.login.LoginFragment
import com.netservex.caf.features.menu_dialog.MenueDialogFragment
import com.netservex.caf.features.offers.OffersListFragment
import com.netservex.caf.features.product_details.ProductDetailsFragment
import com.netservex.caf.features.products_list.AdapterProductsList
import com.netservex.caf.features.subcategories.SubCategoriesFragment
import com.netservex.entities.*
import com.netservex.usecases.usecases.TokenUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList
import java.util.concurrent.TimeUnit

class MainActivity : BaseActivity(), MenueDialogFragment.CustomButtonListener, MainView, AdapterProductsList.customButtonListener,
    PaginationAdapterCallBack {

    var textCartItemCount: TextView? = null
    var menueDialogFragment: MenueDialogFragment? = null
    var disposable: CompositeDisposable= CompositeDisposable()
    var data: PassedDataFromCategoriesToMenu? = null

    private var searchQuery: String? = null

    var adapter: AdapterProductsList? = null
    var staggeredGridLayoutManager: androidx.recyclerview.widget.StaggeredGridLayoutManager? = null
    private var isLoadingV: Boolean = false
    private var isLastPageV: Boolean = false
    private var TOTAL_PAGES: Int = 50
    private lateinit var requestIntervalHandler: RequestIntervalHandler2
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    val totalPageCount = 20
    private val PAGE_START = 1
    private var currentPage = PAGE_START
    private var arrayList: ArrayList<ProductModel>? = null
    var handler: Handler? = null
    private val tryAgainTriggerObserever = Observer<Int> {
        when (it) {
            1 -> searchQuery?.let { presenter?.searchProducts(currentPage, it) }
        }
    }
    private val presenter: MainPresenter by lazy {
        MainImplPresenter(this)
    }

    private var mHandler: Handler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        et_search.setOnClickListener {
            lout_search.visibility = View.VISIBLE
            main_fragment_container.visibility = View.GONE
        }

        et_search.onFocusChangeListener = object: View.OnFocusChangeListener{
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if(hasFocus)
                {
                    lout_search.visibility = View.VISIBLE
                    main_fragment_container.visibility = View.GONE
                }
            }
        }



        val mCartChangeReceiver: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {



                if(p1!!.getIntExtra(KEY_NUMBER_OF_ITEMS, -1) != -1)
                {
                    textCartItemCount?.text = p1!!.getIntExtra(KEY_NUMBER_OF_ITEMS, 0).toString()
                }

                else if(p1!!.getIntExtra(KEY_PLUS_ONE_TO_CART, -1) != -1)
                {
                    textCartItemCount?.text= (textCartItemCount?.text.toString().toInt()+1).toString()
                }

                else if(p1!!.getIntExtra(KEY_MINUS_ONE_TO_CART, -1) != -1)
                {
                    textCartItemCount?.text= (textCartItemCount?.text.toString().toInt()-1).toString()
                }

            }

        }

        LocalBroadcastManager.getInstance(this).registerReceiver(
            mCartChangeReceiver, IntentFilter("cartChanged")
        )


        requestIntervalHandler =
            RequestIntervalHandler2(lout_loading_interval_view_container, this, false)
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

        disposable?.add(
            RxTextView.textChangeEvents(et_search)
                .skipInitialValue()
                .debounce(2000, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith<DisposableObserver<TextViewTextChangeEvent>>(
                    searchQuery()
                )
        )

        currentPage = PAGE_START
        searchQuery?.let { presenter?.searchProducts(currentPage, it) }

        /* disposable = RxBus.subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                if (o instanceof PassedDataFromCategoriesToMenu) {
                    data= (PassedDataFromCategoriesToMenu) o;
                    //do sth with the data .. you can populate a RecycleView for example
                }
            }
        });

        RxBus.publish(data);

        disposable.dispose();
*/setupActionBar()
        getSupportFragmentManager().beginTransaction()
            .add(R.id.main_fragment_container, HomeFragment(), "men").commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        getMenuInflater().inflate(R.menu.main_menu, menu)
        val cartItem = menu.findItem(R.id.action_cart)
        val actionView = MenuItemCompat.getActionView(cartItem)
        textCartItemCount = actionView.findViewById(R.id.cart_badge)
        setupBadge()
        actionView.setOnClickListener { onOptionsItemSelected(cartItem) }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_cart -> {
                if (getSupportFragmentManager().findFragmentByTag("cart_fragment") == null) {
                    val tokenUseCase= TokenUseCase()
                    if(tokenUseCase.isLoggedIn)
                        getSupportFragmentManager().beginTransaction()
                            .add(R.id.main_fragment_container, CartFragment(), "cart_fragment")
                            .addToBackStack("").commit()

                    else
                    getSupportFragmentManager().beginTransaction()
                        .add(R.id.main_fragment_container, LoginFragment(), "login_fragment")
                        .addToBackStack("").commit()
                }
                super.onOptionsItemSelected(item)
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupBadge() {

    }
    fun setupActionBar() {
        setSupportActionBar(main_toolbar)
        val actionBar: ActionBar? = getSupportActionBar()
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)
        actionBar?.setDisplayShowTitleEnabled(false)
        actionBar?.setDisplayUseLogoEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.icon_left)
        main_toolbar!!.setNavigationOnClickListener {
            if (getSupportFragmentManager().findFragmentByTag("menu_dialog") == null) {
                val fm: androidx.fragment.app.FragmentManager = getSupportFragmentManager()
                menueDialogFragment = MenueDialogFragment()
                menueDialogFragment?.setCustomButtonListner(this@MainActivity)
                menueDialogFragment?.show(fm, "menu_dialog")
            }
        }
    }


    override fun onItemClickListner(title: String?, itemId: String?) {
        menueDialogFragment?.dismiss()
        if (itemId == "-1") {
            when (title) {
                "Home" -> if (getSupportFragmentManager().getBackStackEntryCount() >= 1) { //getSupportFragmentManager().beginTransaction().add(R.id.main_fragment_container, new CategoriesFragment(), "men").commit();
                    getSupportFragmentManager().popBackStack(
                        getSupportFragmentManager().getBackStackEntryAt(
                            0
                        ).getId(), androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
                    )
                }
                "Exit" -> {
                    val homeIntent = Intent(Intent.ACTION_MAIN)
                    homeIntent.addCategory(Intent.CATEGORY_HOME)
                    homeIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(homeIntent)
                }
                "Offers" ->
                    getSupportFragmentManager().beginTransaction().add(
                    R.id.main_fragment_container,
                    OffersListFragment(),
                    "offers_fragment"
                ).addToBackStack(null).commit()
            }
        } else getSupportFragmentManager().beginTransaction().add(
            R.id.main_fragment_container,
            SubCategoriesFragment.newInstance(itemId, title),
            "subcategories_fragment"
        ).addToBackStack(null).commit()
    }


    interface SearchListener {
        fun onSearchQueryChange(searchQuery: String)
    }

    companion object {
        var customListenerSearchFragment: SearchListener? = null
        fun setSearchListner(listener: SearchListener?) {
            customListenerSearchFragment = listener
        }
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
        main_fragment_container.visibility = View.VISIBLE
        lout_search.visibility = View.GONE
        supportFragmentManager?.beginTransaction()?.add(R.id.main_fragment_container, ProductDetailsFragment.newInstance(productModel), "")?.addToBackStack("")?.commit()
        et_search.setText("")
    }

    override fun retryPageLoad() {

    }

    private fun populatRecyclerView() {
        arrayList = ArrayList()
        adapter = AdapterProductsList(this, arrayList, SUBCATEGORY_DATA_TYPE)
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
                if(!isLastPageV) {
                    isLoadingV = true
                    currentPage += 1
                    presenter?.searchProducts(currentPage, searchQuery!!)
                }
            }

            override val totalPageCount: Int = TOTAL_PAGES

            override val isLastPage: Boolean = isLastPageV

            override val isLoading: Boolean = isLoadingV
        })
    }

    private fun searchQuery(): DisposableObserver<TextViewTextChangeEvent> {
        return object : DisposableObserver<TextViewTextChangeEvent>() {
            override fun onNext(textViewTextChangeEvent: TextViewTextChangeEvent) {
                Log.d("", "search string: " + textViewTextChangeEvent.text().toString())

                //txtSearchString.setText("Query: " + textViewTextChangeEvent.text().toString());
                searchQuery = textViewTextChangeEvent.text().toString()
                if(textViewTextChangeEvent.text().toString().isNotEmpty()) {

                    main_fragment_container.visibility = View.GONE
                    lout_search.visibility = View.VISIBLE

                    currentPage = 1
                    adapter?.clear()
                    adapter?.notifyDataSetChanged()
                    isLastPageV = false
                    presenter.searchProducts(currentPage, et_search.textValue())
                }

                else
                {
                    adapter?.clear()
                    adapter?.notifyDataSetChanged()
                    main_fragment_container.visibility = View.VISIBLE
                    lout_search.visibility = View.GONE
                }

            }

            override fun onError(e: Throwable) {

            }

            override fun onComplete() {

            }
        }
    }

    override fun onBackPressed() {
        if(lout_search.visibility == View.VISIBLE)
        {
            lout_search.visibility = View.GONE
            main_fragment_container.visibility = View.VISIBLE
        }
        else
            super.onBackPressed()
    }

    override fun onDestroy() {
        disposable?.dispose()
        super.onDestroy()
    }

}