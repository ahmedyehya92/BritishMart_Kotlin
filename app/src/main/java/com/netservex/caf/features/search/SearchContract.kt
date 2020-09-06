package com.netservex.caf.features.search

import androidx.lifecycle.DefaultLifecycleObserver
import com.netservex.caf.core.LoadingHandler
import com.netservex.entities.ProductModel

interface SearchView: LoadingHandler {
    fun addProducts(requests: MutableList<ProductModel>)
    fun setLastPageTrue()
    fun addLoadingFooter()
    fun removeLoadingFooter()
    fun showRetryAdapter()
    fun setIsLoadingFalse()
    fun showEmptyViewForList()
}

interface SearchPresenter: DefaultLifecycleObserver {
    fun searchProducts(page: Int, searchQuery: String)
}