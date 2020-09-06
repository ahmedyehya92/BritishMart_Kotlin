package com.netservex.caf.features.main

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import com.netservex.usecases.usecases.SearchProductsUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainImplPresenter (
    private val view: MainView,
    private var pageSc: Int = 0,
    private val searchProductsUseCase: SearchProductsUseCase = SearchProductsUseCase(),
    private val disposables: CompositeDisposable = CompositeDisposable()
): MainPresenter {
    override fun searchProducts(page: Int, searchQuery: String) {
        this.pageSc = page
        if (page == 1)
            view.showLoading()

        searchProductsUseCase(searchQuery, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (pageSc != 1) {
                    view.removeLoadingFooter()
                    view.setIsLoadingFalse()
                    Log.d("", "page not equal 1: ")
                }

                if (pageSc != 1 && it.products.size == 0) run {
                    view.setLastPageTrue()
                    view.removeLoadingFooter()
                    Log.d("", "last page")
                    Log.d("", "page: " + pageSc)

                }
                else
                    run {

                        if (pageSc == 1) {
                            if (it.products.size == 0)
                                view.showEmptyViewForList()
                            else
                                view.finishLoading()
                        }
                        view.addProducts(it.products)
                        /*if (it.pagination.pageCount > 1)
                            view.addLoadingFooter()
                         */
                    }
            },{
                view.connectionError(it.message?:"Unknown error, try again!")
            })
            .also { disposables.add(it) }


    }

    override fun onDestroy(owner: LifecycleOwner) {
        disposables.dispose()
    }
}