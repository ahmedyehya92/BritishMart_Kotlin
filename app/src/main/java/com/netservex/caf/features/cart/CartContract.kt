package com.netservex.caf.features.cart

import com.netservex.caf.core.LoadingHandler
import com.netservex.entities.CartItemModel

interface CartView: LoadingHandler {
    fun addToItems(requests: MutableList<CartItemModel>)
    fun successfulEditAmount(position: Int, quantity: Int)
    fun failedEditAmount(message: String?)
    fun successfulRemoveProduct(position: Int)
    fun failedRemoveProduct(message: String?)
}

interface CartPresenter {
    fun getCartItems()
    fun editProductAmount(position: Int, productId: Int, quantity: Int)
    fun removeProductFromCart(position: Int, productId: Int)
}