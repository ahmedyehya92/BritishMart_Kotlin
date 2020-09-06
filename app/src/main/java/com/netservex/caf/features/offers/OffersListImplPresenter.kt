package com.netservex.caf.features.offers

import android.util.Log
import com.netservex.entities.OfferModel
import com.netservex.usecases.usecases.GetOffersUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.ArrayList

class OffersListImplPresenter (
    private val view: OffersListView,
    private val getOffersUseCase: GetOffersUseCase = GetOffersUseCase(),
    private val disposables: CompositeDisposable = CompositeDisposable(),
    private var pageSc: Int = 0
): OffersListPresenter {
    override fun getOffersList(page: Int) {
        this.pageSc = page
        if (page == 1)
            view.showLoading()

        getOffersUseCase(page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (pageSc != 1) {
                    view.removeLoadingFooter()
                    view.setIsLoadingFalse()
                    Log.d("", "page not equal 1: ")
                }

                if (pageSc != 1 && it.offers.size == 0) run {
                    view.setLastPageTrue()
                    view.removeLoadingFooter()
                    Log.d("", "last page")
                    Log.d("", "page: " + pageSc)

                }
                else
                    run {

                        if (pageSc == 1) {
                            if (it.offers.size == 0)
                                view.showEmptyViewForList()
                            else
                                view.finishLoading()
                        }
                        view.addOffers(it.offers)
                        /*if (it.pagination.pageCount > 1)
                            view.addLoadingFooter()
                         */
                    }
            },{
                view.connectionError(it.message?:"Unknown error, try again!")
            })
            .also { disposables.add(it) }

    }

}