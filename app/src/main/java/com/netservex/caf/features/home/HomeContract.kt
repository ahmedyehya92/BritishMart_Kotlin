package com.netservex.caf.features.home

import android.content.Context
import androidx.lifecycle.DefaultLifecycleObserver
import com.netservex.entities.CartItemModel
import com.netservex.entities.CategoryModel
import com.netservex.entities.OfferModel
import com.netservex.entities.ProductModel
import java.util.ArrayList

interface HomeView {
    fun addFeaturedProducts(products: MutableList<ProductModel>)
    fun setFeaturedProductsLastPageTrue()
    fun addFeaturedProductsLoadingFooter()
    fun removeFeaturedProductsLoadingFooter()
    fun showFeaturedProductsRetryAdapter()
    fun setFeaturedProductsIsLoadingFalse()
    fun showFeaturedProductsEmptyViewForList()

    fun showFeaturedProductsLoading()
    fun finishFeaturedProductsLoading()
    fun connectionFeaturedProductsError(message: String?="")
    fun failedFeaturedProductsLoading(message: Any)
    fun numberOfCartItems(cartItems: Int)

    fun showCategoriesLoading()
    fun finishCategoriesLoading()
    fun connectionCategoriesError(message: String? = null)
    fun addMoreCategoriesToAdapter(list: ArrayList<CategoryModel>)

    fun showOfferLoading()
    fun finishOfferLoading()
    fun connectionOfferError(message: String? = null)
    fun addOfferDetails(offer: ProductModel?)
}

interface HomePresenter: DefaultLifecycleObserver {
    fun getFeaturedProductsList(page: Int)
    fun loadCategories()
    fun loadMainOffer()
}