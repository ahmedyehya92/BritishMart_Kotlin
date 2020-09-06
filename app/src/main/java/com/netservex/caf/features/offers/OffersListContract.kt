package com.netservex.caf.features.offers

import com.netservex.caf.core.LoadingHandler
import com.netservex.entities.OfferModel
import com.netservex.entities.ProductModel

interface OffersListView: LoadingHandler {
    fun addOffers(requests: MutableList<ProductModel>)
    fun setLastPageTrue()
    fun addLoadingFooter()
    fun removeLoadingFooter()
    fun showRetryAdapter()
    fun setIsLoadingFalse()
    fun showEmptyViewForList()
}

interface OffersListPresenter {
    fun getOffersList(page: Int)
}