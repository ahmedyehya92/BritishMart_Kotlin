package com.netservex.caf.features.products_list

import androidx.lifecycle.DefaultLifecycleObserver
import com.netservex.caf.core.LoadingHandler
import com.netservex.entities.ProductModel

interface ProductsListView: LoadingHandler {
    fun addProducts(requests: MutableList<ProductModel>)
    fun setLastPageTrue()
    fun addLoadingFooter()
    fun removeLoadingFooter()
    fun showRetryAdapter()
    fun setIsLoadingFalse()
    fun showEmptyViewForList()


}

interface ProductsListPresenter: DefaultLifecycleObserver {
    fun getProductsList(page: Int, categoryId: String)
}