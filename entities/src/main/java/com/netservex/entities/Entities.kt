package com.netservex.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName

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
    val discount_percentage: String?,
    val gallery: MutableList<Any?>?
)

data class OfferModel(
    val id: String? = null,
    val imgUrl: String? = null,
    var title: String? = null
)

data class CartItemModel (
    val productName: String? = null,
    var quantity: Int? = null,
    val priceForUnit: Int? = null,
    val ImageUrl: String? = null,
    val id: String? = null,
    var priceForTotalQuantity: Int? = priceForUnit?.let { quantity?.times(it) }
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