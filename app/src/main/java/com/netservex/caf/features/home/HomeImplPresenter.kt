package com.netservex.caf.features.home

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import com.netservex.usecases.usecases.GetCartItemsUseCase
import com.netservex.usecases.usecases.GetCategoriesUseCase
import com.netservex.usecases.usecases.GetFeaturedProductsUseCase
import com.netservex.usecases.usecases.GetMainOfferUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class HomeImplPresenter (
    private val view: HomeView,
    private var pageSc: Int = 0,
    private val getFeaturedProductsUseCase: GetFeaturedProductsUseCase= GetFeaturedProductsUseCase(),
    private val getCartItemsUseCase: GetCartItemsUseCase = GetCartItemsUseCase(),
    private val getCategoriesUseCase: GetCategoriesUseCase = GetCategoriesUseCase(),
    private val getMainOfferUseCase: GetMainOfferUseCase= GetMainOfferUseCase(),
    private val disposables: CompositeDisposable = CompositeDisposable()

): HomePresenter {
    override fun getFeaturedProductsList(page: Int) {
        this.pageSc = page
        if (page == 1)
            view.showFeaturedProductsLoading()

        getFeaturedProductsUseCase(page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

                getCartItemsUseCase()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        Log.e("getCartItems","success")
                        view.numberOfCartItems(it.cartItems.size)
                    },{
                        Log.e("getCartItems",it.message?:"error")
                    })
                    .also { disposables.add(it) }

               /* if (pageSc != 1) {
                    view.removeFeaturedProductsLoadingFooter()
                    view.setFeaturedProductsIsLoadingFalse()
                    Log.d("", "page not equal 1: ")
                }

                if (pageSc != 1 && it.featured_products.size == 0) run {
                    view.setFeaturedProductsLastPageTrue()
                    view.removeFeaturedProductsLoadingFooter()
                    Log.d("", "last page")
                    Log.d("", "page: " + pageSc)

                }
                else*/
                    run {

                        if (pageSc == 1) {

                            if (it.featured_products.size == 0)
                                view.showFeaturedProductsEmptyViewForList()
                            else
                                view.finishFeaturedProductsLoading()
                        }
                        view.addFeaturedProducts(it.featured_products)
                        /*if (it.pagination.pageCount > 1)
                            view.addLoadingFooter()
                         */
                    }
            },{
                view.connectionFeaturedProductsError(it.message?:"Unknown error, try again!")
            })
            .also { disposables.add(it) }
    }

    override fun loadCategories() {
        view.showCategoriesLoading()
        getCategoriesUseCase()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.finishCategoriesLoading()
                view.addMoreCategoriesToAdapter(it.categories)

            },{
                view.connectionCategoriesError(it.message)
            })
            .also { disposables.add(it) }
    }

    override fun loadMainOffer() {
        view.showOfferLoading()
        getMainOfferUseCase().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.finishOfferLoading()
                view.addOfferDetails(it.offers[0])
            },{
                view.connectionOfferError(it.message)
            }).also { disposables.add(it) }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        disposables.dispose()
    }
}