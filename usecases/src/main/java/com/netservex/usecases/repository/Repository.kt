package com.netservex.usecases.repository

import com.netservex.entities.*
import com.netservex.usecases.DutchMartApis
import com.netservex.usecases.PreferencesHelper
import com.netservex.usecases.DUTCH_MART_APIS
import io.reactivex.Single
import retrofit2.Call


val repository: Repository by lazy { RepositoryImplementer() }

interface Repository {
    fun login (email: String, password: String): Single<ResponseLogin>
    fun forgetPassword (email: String): Single<ResponseForgetPassword>
    fun getTokenHeader(refreshToken: String): Call<LoginResponse>
    fun setLoggedInStatus(status:Boolean)
    fun getLoggedInStatus(): Boolean
    fun setToken(token: String)
    fun getToken():String
    fun setUserName(userName:String)
    fun setPassword(password: String)
    fun getUserName(): String
    fun getPassword(): String
    fun getRefreshToken(): String
    fun setRefreshToken(refreshToken: String)
    fun getCategories(): Single<ResponseGetCategories>
    fun getSubCategories(categoryId: String, page: Int): Single<ResponseGetSubCategories>
    fun getCategoryProducts(categoryId: String, page: Int): Single<ResponseGetCategoryProducts>
    fun getProductDetails(productId: Int): Single<ResponseProductDetails>
    fun register(firstName: String, lastName: String, email: String, password: String, passwordConfirmation: String, mobileNumber: String): Single<ResponseRegister>
    fun getTokenHeader(email: String, password: String): Call<ResponseLogin>
    fun addProductToCart(productId: Int, quantity: Int): Single<ResponseAddProductToCart>
    fun removeProductFromCart(productId: Int): Single<ResponseAddProductToCart>
    fun getOffers(page: Int): Single<ResponseGetOffers>
    fun favoriteAction(productId: Int): Single<ResponseFavoriteAction>
    fun getCartItems(): Single<ResponseGetCartItems>
    fun searchProducts(searchQuery: String, page: Int): Single<ResponseGetSearchProducts>
    fun getFeaturedProducts(page: Int): Single<ResponseGetFeaturedProducts>
    fun getMainOffer(): Single<ResponseGetOffers>
}

class RepositoryImplementer (
    private val server: DutchMartApis = DUTCH_MART_APIS,
    private val preferencesHelper: PreferencesHelper = PreferencesHelper()

) : Repository
{
    override fun setRefreshToken(refreshToken: String) {
        preferencesHelper.refreshToken = refreshToken
    }

    override fun getCategories(): Single<ResponseGetCategories> = server.getCategories()
    override fun getSubCategories(categoryId: String, page: Int): Single<ResponseGetSubCategories> = server.getSubCategories(categoryId, page)
    override fun getCategoryProducts(
        categoryId: String,
        page: Int
    ): Single<ResponseGetCategoryProducts> = server.getProductsOfCategory(categoryId, page)

    override fun getProductDetails(productId: Int): Single<ResponseProductDetails> = server.getProductDetails(productId)
    override fun register(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        passwordConfirmation: String,
        mobileNumber: String
    ): Single<ResponseRegister> = server.register(firstName, lastName, email, password, passwordConfirmation, mobileNumber)

    override fun addProductToCart(productId: Int, quantity: Int): Single<ResponseAddProductToCart> = server.addProductToCart(productId, quantity)
    override fun removeProductFromCart(productId: Int): Single<ResponseAddProductToCart> = server.removeProductFromCart(productId)

    override fun getOffers(page: Int): Single<ResponseGetOffers> = server.getOffers(page)
    override fun favoriteAction(productId: Int): Single<ResponseFavoriteAction> = server.addRemoveFavorites(productId)
    override fun getCartItems(): Single<ResponseGetCartItems> = server.getCartItems()
    override fun searchProducts(searchQuery: String, page: Int): Single<ResponseGetSearchProducts> = server.getProductsBySearch(searchQuery, page)
    override fun getFeaturedProducts(page: Int): Single<ResponseGetFeaturedProducts> = server.getFeaturedProducts(page)
    override fun getMainOffer(): Single<ResponseGetOffers> = server.getMainOffer()

    override fun getRefreshToken(): String {
        return preferencesHelper.refreshToken
    }


    override fun getTokenHeader(refreshToken: String): Call<LoginResponse> = server.getTokenInterceptor(RefreshTokenFields(refreshToken))
    override fun getTokenHeader(email: String, password: String): Call<ResponseLogin> = server.getTokenInterceptor(email, password)
    override fun setUserName(userName: String) {
        preferencesHelper.username = userName
    }

    override fun setPassword(password: String) {
        preferencesHelper.password = password
    }

    override fun getUserName(): String {
        return preferencesHelper.username
    }

    override fun getPassword(): String {
        return preferencesHelper.password
    }

    override fun getLoggedInStatus(): Boolean {
        return preferencesHelper.isLoggedIn
    }

    override fun setToken(token: String) {
        preferencesHelper.token = token
    }

    override fun getToken(): String {
        return preferencesHelper.token
    }

    override fun setLoggedInStatus(status: Boolean) {
        preferencesHelper.isLoggedIn = status
    }

    override fun login(email: String, password: String) = server.login(email, password)
    override fun forgetPassword(email: String): Single<ResponseForgetPassword> = server.forgetPassword(email)


}


