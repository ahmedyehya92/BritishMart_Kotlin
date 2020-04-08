package com.netservex.usecases

import com.netservex.entities.*
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import android.util.Log
import com.netservex.usecases.usecases.TokenUseCase
import okhttp3.Interceptor
import retrofit2.Call
import java.io.IOException
import okhttp3.OkHttpClient


private const val SERVER_BASE_URL = "http://api.dutchmart.co/api/"

private val retrofit: Retrofit by lazy {

    val httpClient = OkHttpClient.Builder()
    httpClient.addNetworkInterceptor(AuthorizationInterceptor())
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
    @POST("login_check")
    fun login(@Body userObject: LoginFields)
            : Single<LoginResponse>


    @POST("token/refresh")
    fun getTokenInterceptor(@Body refreshToken: RefreshTokenFields)
            : Call<LoginResponse>

    @GET("categories")
    fun getCategories(): Single<ResponseGetCategories>

    @GET("subcategories/{category_id}")
    fun getSubCategories(@Path("category_id") categoryId: String, @Query("p") page: Int): Single<ResponseGetSubCategories>

    @GET("categoryproducts/{category_id}")
    fun getProductsOfCategory(@Path("category_id") categoryId: String, @Query("p") page: Int): Single<ResponseGetCategoryProducts>


}


class AuthorizationInterceptor(private val tokenUseCase: TokenUseCase = TokenUseCase()) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        var mainResponse = chain.proceed(chain.request())
        val mainRequest = chain.request()

        if (tokenUseCase.isLoggedIn) {
            // if response code is 401 or 403, 'mainRequest' has encountered authentication error
            if (mainResponse.code() == 401 || mainResponse.code() == 403) {
                //val authKey = getAuthorizationHeader(session.getEmail(), session.getPassword())
                // request to login API to get fresh token
                // synchronously calling login API




                val tokenResponse: retrofit2.Response<LoginResponse> = tokenUseCase.getTokenInterceptor(tokenUseCase.refreshToken).execute()

                if (tokenResponse.isSuccessful) {
                    // login request succeed, new token generated
                    val authorization = tokenResponse.body()
                    // save the new token
                    tokenUseCase.token = authorization!!.token
                    Log.i("", "interceptor done")
                    // retry the 'mainRequest' which encountered an authentication error
                    // add new token into 'mainRequest' header and request again

                    Log.i("", "token = ${tokenUseCase.token}")

                    val builder = mainRequest.newBuilder().header("Authorization", tokenUseCase.token)
                        .method(mainRequest.method(), mainRequest.body())


                    mainResponse = chain.proceed(builder.build())
                }



            }
        }

        return mainResponse
    }
/*
    companion object {

        /**
         * this method is API implemetation specific
         * might not work with other APIs
         */
        fun getAuthorizationHeader(email: String, password: String): String {
            val credential = "$email:$password"
            return "Basic " + Base64.encodeToString(credential.toByteArray(), Base64.DEFAULT)
        }
    }
    */
}
