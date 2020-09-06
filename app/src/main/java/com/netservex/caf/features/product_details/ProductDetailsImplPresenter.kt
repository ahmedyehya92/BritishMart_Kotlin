package com.netservex.caf.features.product_details

import androidx.lifecycle.LifecycleOwner
import android.util.Log
import com.netservex.entities.ResponseAddProductToCart
import com.netservex.entities.ResponseFavoriteAction
import com.netservex.entities.ResponseProductDetails
import com.netservex.usecases.usecases.AddProductToCartUseCase
import com.netservex.usecases.usecases.FavoriteActionUseCase
import com.netservex.usecases.usecases.GetProductDetailsUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

class ProductDetailsImplPresenter (
    private val view: ProductDetailsView,
    private val getProductDetailsUseCase: GetProductDetailsUseCase= GetProductDetailsUseCase(),
    private val addProductToCartUseCase: AddProductToCartUseCase= AddProductToCartUseCase(),
    private val favoriteActionUseCase: FavoriteActionUseCase = FavoriteActionUseCase(),
    private val TAG: String = ProductDetailsImplPresenter::class.java.simpleName,
    private val disposables: CompositeDisposable = CompositeDisposable()
): ProductDetailsPresenter
{
    override fun getProductDetails(productId: Int) {
        view.showLoading()
        getProductDetailsUseCase(productId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({handlegetProductDetailsSuccessfulResponse(it) },{handlegetProductDetailsErrorResponse(it)})
            .also { disposables.add(it) }
    }

    private fun handlegetProductDetailsErrorResponse(throwable: Throwable) {
        view.connectionError(throwable.message?:"Unknown error, Try again!")
    }

    private fun handlegetProductDetailsSuccessfulResponse(responseProductDetails: ResponseProductDetails) {
        view.finishLoading()
        view.addProductDetails(responseProductDetails.details)

    }

    override fun addProductToCart(productId: Int, quantity: Int) {
        view.showLoading()
        addProductToCartUseCase(productId, quantity)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({handleAddProductToCartSuccessfulResponse(it) },{handleAddProductToCartErrorResponse(it)})
            .also { disposables.add(it) }
    }

    private fun handleAddProductToCartErrorResponse(throwable: Throwable) {
        Log.e(TAG, throwable.message?:"Unknown error, Try again!")
        view.finishLoading()
        if(throwable is HttpException)
            view.failedAddProductToCart(throwable.message?:"Unknown error, Try again!", throwable.code())
        else
            view.failedAddProductToCart(throwable.message?:"Unknown error, Try again!")
    }

    private fun handleAddProductToCartSuccessfulResponse(responseAddProductToCart: ResponseAddProductToCart) {
        Log.e(TAG, "success add to cart")
        view.finishLoading()
        view.successfulAddProductToCart()

    }

    override fun favoriteAction(productId: Int) {
        view.showLoading()
        favoriteActionUseCase(productId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({handleFavoriteActionSuccessfulResponse(it) },{handleFavoriteActionErrorResponse(it)})
            .also { disposables.add(it) }
    }

    private fun handleFavoriteActionErrorResponse(throwable: Throwable) {
        Log.e(TAG, throwable.message?:"Unknown error, Try again!")
        view.finishLoading()
        if(throwable is HttpException)
            view.failedFavoriteAction(throwable.message?:"Unknown error, Try again!", throwable.code())
        else
            view.failedFavoriteAction(throwable.message?:"Unknown error, Try again!")
    }

    private fun handleFavoriteActionSuccessfulResponse(responseFavoriteAction: ResponseFavoriteAction) {
        Log.e(TAG, "success favorite action")
        view.finishLoading()
        view.successfulFavoriteAction(responseFavoriteAction.responseBody.favored)

    }

    override fun onDestroy(owner: LifecycleOwner) {
        disposables.dispose()
    }
}