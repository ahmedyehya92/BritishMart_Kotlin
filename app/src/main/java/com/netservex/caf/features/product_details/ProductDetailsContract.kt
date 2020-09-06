package com.netservex.caf.features.product_details

import androidx.lifecycle.DefaultLifecycleObserver
import com.netservex.caf.core.LoadingHandler
import com.netservex.entities.ProductModel

interface ProductDetailsView: LoadingHandler
{
    fun addProductDetails(productDetails: ProductModel?)
    fun successfulAddProductToCart()
    fun failedAddProductToCart(message: String?, code:Int? = null)
    fun successfulFavoriteAction(favored: Boolean)
    fun failedFavoriteAction(message: String?, code:Int? = null)
}

interface ProductDetailsPresenter: DefaultLifecycleObserver
{
    fun getProductDetails(productId: Int)
    fun addProductToCart(productId: Int, quantity: Int)
    fun favoriteAction(productId: Int)
}