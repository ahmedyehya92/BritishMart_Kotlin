package com.netservex.caf.features.cart

import android.util.Log
import com.netservex.usecases.usecases.AddProductToCartUseCase
import com.netservex.usecases.usecases.GetCartItemsUseCase
import com.netservex.usecases.usecases.RemoveProductFromCartUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CartImplPresenter(
    private val view: CartView,
    private val getCartItemsUseCase: GetCartItemsUseCase = GetCartItemsUseCase(),
    private val disposables: CompositeDisposable = CompositeDisposable(),
    private val editProductAmountUseCase: AddProductToCartUseCase = AddProductToCartUseCase(),
    private val removeProductFromCartUseCase: RemoveProductFromCartUseCase = RemoveProductFromCartUseCase()
):  CartPresenter{
    private val TAG = this::class.java.simpleName
    override fun getCartItems() {
        view.showLoading()
        getCartItemsUseCase()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.e("getCartItems","success")
                view.finishLoading()
                view.addToItems(it.cartItems)
            },{
                Log.e("getCartItems",it.message?:"error")
                view.connectionError(it.message?: "Unknown error")
            })
            .also { disposables.add(it) }

    }

    override fun editProductAmount(position: Int, productId: Int, quantity: Int) {
        view.showLoading()
        editProductAmountUseCase(productId, quantity)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({handleEditProductAmountSuccessfulResponse(position, quantity) },{handleEditProductAmountErrorResponse(it)})
            .also { disposables.add(it) }
    }


    private fun handleEditProductAmountErrorResponse(throwable: Throwable) {
        Log.e(TAG, throwable.message?:"Unknown error, Try again!")
        view.finishLoading()
        view.failedEditAmount(throwable.message?:"Unknown error, Try again!")
    }

    private fun handleEditProductAmountSuccessfulResponse(position: Int, quantity: Int) {
        Log.e(TAG, "success add to cart")
        view.finishLoading()
        view.successfulEditAmount(position, quantity)

    }

    override fun removeProductFromCart(position: Int, productId: Int) {
        view.showLoading()
        removeProductFromCartUseCase(productId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({handleRemoveProductResponse(position) },{handleRemoveProductErrorResponse(it)})
            .also { disposables.add(it) }
    }


    private fun handleRemoveProductErrorResponse(throwable: Throwable) {
        Log.e(TAG, throwable.message?:"Unknown error, Try again!")
        view.finishLoading()
        view.failedRemoveProduct(throwable.message?:"Unknown error, Try again!")
    }

    private fun handleRemoveProductResponse(position: Int) {
        Log.e(TAG, "success add to cart")
        view.finishLoading()
        view.successfulRemoveProduct(position)

    }


}