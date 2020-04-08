package com.netservex.caf.features.products_list

import android.arch.lifecycle.LifecycleOwner
import android.util.Log
import com.netservex.entities.ProductModel
import com.netservex.usecases.usecases.GetProductsOfCategoryUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.ArrayList

class ProductsListImplPresenter (
    private val view: ProductsListView,
    private var pageSc: Int = 0,
    private val getProductsOfCategoryUseCase: GetProductsOfCategoryUseCase= GetProductsOfCategoryUseCase(),
    private val disposables: CompositeDisposable = CompositeDisposable()

): ProductsListPresenter {
    override fun getProductsList(page: Int, categoryId: String) {
        this.pageSc = page
        if (page == 1)
            view.showLoading()

        getProductsOfCategoryUseCase(categoryId, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (pageSc != 1) {
                    view.removeLoadingFooter()
                    view.setIsLoadingFalse()
                    Log.d("", "page not equal 1: ")
                }

                if (pageSc != 1 && it.categoryproducts.size == 0) run {
                    view.setLastPageTrue()
                    view.removeLoadingFooter()
                    Log.d("", "last page")
                    Log.d("", "page: " + pageSc)

                }
                else
                    run {

                        if (pageSc == 1) {
                            if (it.categoryproducts.size == 0)
                                view.showEmptyViewForList()
                            else
                                view.finishLoading()
                        }
                        view.addProducts(it.categoryproducts)
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