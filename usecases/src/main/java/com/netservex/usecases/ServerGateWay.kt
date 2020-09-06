package com.netservex.usecases

import android.util.Log
import com.netservex.entities.*
import com.netservex.usecases.usecases.TokenUseCase
import io.reactivex.Single
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.io.IOException


private const val SERVER_BASE_URL = "http://api.dutchmart.co/api/"

private val retrofit: Retrofit by lazy {

    val httpClient = OkHttpClient.Builder()
    httpClient.addInterceptor(AnotherHeadersInterceptor()).
    addInterceptor(AuthorizationInterceptor())
    //httpClient.addInterceptor(LogJsonInterceptor())

    Retrofit.Builder()
        .baseUrl(SERVER_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(httpClient.build())
        .build()
}

val DUTCH_MART_APIS: DutchMartApis by lazy {
    retrofit.create(DutchMartApis::class.java)
}

interface DutchMartApis {

    @POST("token/refresh")
    fun getTokenInterceptor(@Body refreshToken: RefreshTokenFields
    ): Call<LoginResponse>

    @POST("login")
    fun getTokenInterceptor(@Query("email") email: String,
                            @Query("password") passwowrd: String
    ): Call<ResponseLogin>

    @POST("forgetpassword")
    fun forgetPassword(@Query("email") email: String
    ): Single<ResponseForgetPassword>

    @GET("categories")
    fun getCategories(
    ): Single<ResponseGetCategories>

    @GET("subcategories/{category_id}")
    fun getSubCategories(@Path("category_id") categoryId: String,
                         @Query("p") page: Int
    ): Single<ResponseGetSubCategories>

    @GET("categoryproducts/{category_id}")
    fun getProductsOfCategory(@Path("category_id") categoryId: String,
                              @Query("p") page: Int
    ): Single<ResponseGetCategoryProducts>

    @GET("product_details/{product_id}")
    fun getProductDetails(@Path("product_id") productId: Int
    ): Single<ResponseProductDetails>

    @POST("register")
    fun register(@Query("fname") firstName: String,
                 @Query("lname") lastName: String,
                 @Query("email") email: String,
                 @Query("password") password: String,
                 @Query("password_confirmation") passwordConfirmation: String,
                 @Query("phone") mobileNumber: String
    ): Single<ResponseRegister>

    @POST("login")
    fun login(
        @Query("email") email: String,
        @Query("password") passwowrd: String
    ): Single<ResponseLogin>


    @POST("additemtocart")
    fun addProductToCart(
        @Query("product") product: Int,
        @Query("quantity") quantity: Int
    ): Single<ResponseAddProductToCart>

    @POST("removeitemfromcart")
    fun removeProductFromCart(
        @Query("product") product: Int
    ): Single<ResponseAddProductToCart>

    @GET("offers")
    fun getOffers(
        @Query("p") page: Int
    ):Single<ResponseGetOffers>

    @POST("addremovefavourites")
    fun addRemoveFavorites(
        @Query("product") product: Int
    ): Single<ResponseFavoriteAction>

    @GET("viewcart")
    fun getCartItems(
    ): Single<ResponseGetCartItems>

    @GET("allproducts/{search_query}")
    fun getProductsBySearch(@Path("search_query") searchQuery: String,
                              @Query("p") page: Int
    ): Single<ResponseGetSearchProducts>

    @GET("featuredproducts")
    fun getFeaturedProducts(@Query("p") page: Int
    ): Single<ResponseGetFeaturedProducts>

    @GET("mainoffer")
    fun getMainOffer(
    ): Single<ResponseGetOffers>

    /*@GET("saveorder")
    fun saveOrder()*/

}


class AuthorizationInterceptor(private val tokenUseCase: TokenUseCase = TokenUseCase()) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        var mainResponse = chain.proceed(chain.request())
        val mainRequest = chain.request()

        Log.i("Interceptor", "is logged In = ${tokenUseCase.isLoggedIn}")

        if (tokenUseCase.isLoggedIn) {
            // if response code is 401 or 403, 'mainRequest' has encountered authentication error
            Log.e("Interceptor", "${mainResponse.code()}")
            if (mainResponse.code() == 401 || mainResponse.code() == 403 ) {
                
                //val authKey = getAuthorizationHeader(session.getEmail(), session.getPassword())
                // request to login API to get fresh token
                // synchronously calling login API


                //val tokenResponse: retrofit2.Response<LoginResponse> = tokenUseCase.getTokenInterceptor(tokenUseCase.refreshToken).execute()

                val tokenResponse: retrofit2.Response<ResponseLogin> = tokenUseCase.getTokenInterceptor(tokenUseCase.userName, tokenUseCase.password).execute()


                if (tokenResponse.isSuccessful) {
                    // login request succeed, new token generated
                    val authorization = tokenResponse.body()
                    // save the new token
                    tokenUseCase.token = authorization!!.access_token
                    Log.i("", "interceptor done")
                    // retry the 'mainRequest' which encountered an authentication error
                    // add new token into 'mainRequest' header and request again

                    Log.i("", "token = ${tokenUseCase.token}")

                    val builder = mainRequest.newBuilder().header("Authorization", tokenUseCase.token)
                        .method(mainRequest.method(), mainRequest.body())


                    mainResponse = chain.proceed(builder.build())
                }

                else
                {
                    Log.i("interceptor", "interceptor error= ${tokenResponse.errorBody().toString()}")
                }



            }
        }

        return mainResponse
    }

}

class AnotherHeadersInterceptor(private val tokenUseCase: TokenUseCase = TokenUseCase()) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        Log.i("Interceptor", "is logged In = ${tokenUseCase.isLoggedIn}")

        val original: Request = chain.request()

        val request: Request = original.newBuilder()
            .header("Accept", "application/json")
            .apply {
                if (tokenUseCase.isLoggedIn)
                    this.header("Authorization", tokenUseCase.token)
            }
            .method(original.method(), original.body())
            .build()

        return chain.proceed(request)

    }
}
