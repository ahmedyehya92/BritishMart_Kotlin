package com.netservex.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LoginFields(
    val username: String,
    val password: String
)

data class LoginResponse(
    @field:SerializedName("token") val token:String
)


data class Response<T>(
    val message: String,
    val result: T,
    val status: Boolean,
    val status_code: Int
)


data class Pagination(
    val current: Int,
    val currentItemCount: Int,
    val endPage: Int,
    val first: Int,
    val firstItemNumber: Int,
    val firstPageInRange: Int,
    val last: Int,
    val lastItemNumber: Int,
    val lastPageInRange: Int,
    val next: Int,
    val numItemsPerPage: Int,
    val pageCount: Int,
    val pageRange: Int,
    val pagesInRange: List<Int>,
    val startPage: Int,
    val totalCount: Int
)

data class ResponseGetCategories(
    val categories: ArrayList<CategoryModel>
)

data class ResponseRegister(
    val user: User?
)

data class ResponseLogin(
    val user: User?,
    val access_token: String
)

data class User (
    val code: String?,
    val id: String?,
    val name: String?,
    val email: String?,
    val phone: String?,
    val msg: String?
)

data class ResponseAddProductToCart(
    val response: String
)

data class CategoryModel(
    var name: String? = null,
    val id: String? = null,
    val image: String? = null,
    val im: Bitmap? = null
)

data class ResponseGetSubCategories(
    val subcategories: MutableList<SubCategoryModel>
)

data class SubCategoryModel(
    var name: String? = null,
    val id: String? = null,
    val image: String? = null
)

data class ResponseGetCategoryProducts(
    val categoryproducts: MutableList<ProductModel>
)

data class ResponseGetFeaturedProducts(
    val featured_products: MutableList<ProductModel>
)

data class ResponseGetSearchProducts(
    val products: MutableList<ProductModel>
)

data class ResponseProductDetails(
    val details: ProductModel?
)

data class OrderData(
    val firstName: String,
    val secondName: String,
    val email: String,
    val address: String,
    val phone: String
): Serializable

data class ProductModel(
    var name: String? = null,
    val id: String? = null,
    val image: String? = null,
    val categoryid: String?,
    val category: String?,
    val intro: String?,
    val details: String?,
    val price: String?,
    val oprice: String?,
    val offer_start_date: String?,
    val offer_start_time: String?,
    val offer_end_date: String?,
    val offer_end_time: String?,
    val offer_start_timestamp: String?,
    val offer_end_timestamp: String?,
    val discount_percentage: String?,
    val gallery: MutableList<Any?>?,
    var isliked: Boolean = false
): Serializable

data class ResponseGetOffers(
    @SerializedName("products") val offers: MutableList<ProductModel>
)


data class OfferModel(
    var name: String? = null,
    val id: String? = null,
    val image: String? = null,
    val categoryid: String?,
    val category: String?,
    val intro: String?,
    val details: String?,
    val price: String?,
    val oprice: String?,
    val offer_start_date: String?,
    val offer_start_time: String?,
    val offer_end_date: String?,
    val offer_end_time: String?,
    val offer_start_timestamp: String?,
    val offer_end_timestamp: String?,
    val discount_percentage: String?,
    val gallery: MutableList<Any?>?,
    var isliked: Boolean = false
)

data class ResponseGetCartItems(
    @SerializedName("cartitems") val cartItems: MutableList<CartItemModel>
)

data class CartItemModel (
    val name: String? = null,
    var quantity: Int = 0,
    @SerializedName("price") val priceOfUnit: Float = 0.0F,
    val image: String? = null,
    val id: String? = null,
    var priceForTotalQuantity: Float = quantity.times(priceOfUnit)
)

data class ResponseFavoriteAction(
    @SerializedName("response") val responseBody: ResponseFavoriteActionContent
)

data class ResponseFavoriteActionContent (
    @SerializedName("exist") val favored: Boolean = false
)

data class ResponseForgetPassword (
    val response: ResponseForgetPasswordEnter
)

data class ResponseForgetPasswordEnter (
    val msg: String
)






data class RefreshTokenFields(
    val refresh_token: String
)



@Entity
data class SeatDB(
    @PrimaryKey(autoGenerate = true)
    val roomId: Int,

    @field:SerializedName("color")val color: String,
    @field:SerializedName("confirmed")val confirmed: Boolean,
    @field:SerializedName("reserved")val reserved: Boolean,
    @field:SerializedName("seatcode")val seatcode: String,
    @field:SerializedName("isseated")val seated: Boolean,
    @field:SerializedName("visibility")val visibility: Boolean
)


const val SUBCATEGORY_DATA_TYPE = 100
const val PRODUCT_DATA_TYPE = 101

// loading handler constants
const val LOADING = 0
const val ERRORCONNECTION = 1
const val ERRORFROMSERVER= 2
const val ERRORUNKNOWN= 3
const val FINISH = 4
const val DONE = 5
const val EMPTY_LIST = 6



// for navigation
const val LOGIN_SCREEN = 100
const val EVENTS_SEARCH_SCREEN = 101
const val SEATS_SCREEN = 102


//for SharedPreference
const val PREF_KEY_LOGGEDIN = "PREF_KEY_LOGGEDIN"
const val PREF_KEY_TOKEN = "PREF_KEY_TOKEN"
const val PREF_KEY_REFRESH_TOKEN = "PREF_KEY_REFRESH_TOKEN"
const val PREF_KEY_USERNAME = "PREF_KEY_USERNAME"
const val PREF_KEY_PASSWORD = "PREF_KEY_PASSWORD"



const val REQUEST_FETCH_EVENTS = 1001
const val REQUEST_FETCH_EVENT_GUESTS = 1002

const val REQUEST_FETCH_SEATS = 1050

const val ACTION_SHOW_SEAT_LOCATION = "ACTION_SHOW_SEAT_LOCATION"

const val EXTRA_AUDIENCE = "EXTRA_AUDIENCE"

const val BASE_URL_DIRECT_PAYMENT = "https://apitest.myfatoorah.com/"
const val TOKEN_DIRECT_PAYMENT = "fVysyHHk25iQP4clu6_wb9qjV3kEq_DTc1LBVvIwL9kXo9ncZhB8iuAMqUHsw-vRyxr3_jcq5-bFy8IN-C1YlEVCe5TR2iCju75AeO-aSm1ymhs3NQPSQuh6gweBUlm0nhiACCBZT09XIXi1rX30No0T4eHWPMLo8gDfCwhwkbLlqxBHtS26Yb-9sx2WxHH-2imFsVHKXO0axxCNjTbo4xAHNyScC9GyroSnoz9Jm9iueC16ecWPjs4XrEoVROfk335mS33PJh7ZteJv9OXYvHnsGDL58NXM8lT7fqyGpQ8KKnfDIGx-R_t9Q9285_A4yL0J9lWKj_7x3NAhXvBvmrOclWvKaiI0_scPtISDuZLjLGls7x9WWtnpyQPNJSoN7lmQuouqa2uCrZRlveChQYTJmOr0OP4JNd58dtS8ar_8rSqEPChQtukEZGO3urUfMVughCd9kcwx5CtUg2EpeP878SWIUdXPEYDL1eaRDw-xF5yPUz-G0IaLH5oVCTpfC0HKxW-nGhp3XudBf3Tc7FFq4gOeiHDDfS_I8q2vUEqHI1NviZY_ts7M97tN2rdt1yhxwMSQiXRmSQterwZWiICuQ64PQjj3z40uQF-VHZC38QG0BVtl-bkn0P3IjPTsTsl7WBaaOSilp4Qhe12T0SRnv8abXcRwW3_HyVnuxQly_OsZzZry4ElxuXCSfFP2b4D2-Q" // Merchant Email


const val KEY_NUMBER_OF_ITEMS="KEY_NUMBER_OF_ITEMS"
const val KEY_PLUS_ONE_TO_CART= "KEY_PLUS_ONE_TO_CART"
const val KEY_MINUS_ONE_TO_CART= "KEY_MINUS_ONE_TO_CART"