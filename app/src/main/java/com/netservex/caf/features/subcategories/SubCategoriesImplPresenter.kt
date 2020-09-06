package com.netservex.caf.features.subcategories

import androidx.lifecycle.LifecycleOwner
import android.util.Log
import com.netservex.entities.CategoryModel
import com.netservex.usecases.usecases.GetSubCategoriesUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class SubCategoriesImplPresenter (
    private val view: SubCategoriesView,
    private var pageSc: Int = 0,
    private val getSubCategoriesUseCase: GetSubCategoriesUseCase = GetSubCategoriesUseCase(),
    private val disposables: CompositeDisposable = CompositeDisposable()
): SubCategoriesPresenter {
    private val TAG = this::class.java.simpleName
    override fun getSubCategories(page: Int, categoryId: String) {

        this.pageSc = page

        if (page == 1)
            view.showLoading()

        getSubCategoriesUseCase(categoryId, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (pageSc != 1) {
                    view.removeLoadingFooter()
                    view.setIsLoadingFalse()
                    Log.d("", "page not equal 1: ")
                }

                if (pageSc != 1 && it.subcategories.size == 0) run {
                    view.setLastPageTrue()
                    view.removeLoadingFooter()
                    Log.d("", "last page")
                    Log.d("", "page: " + pageSc)

                }
                else
                    run {
                        Log.d(TAG, "page: " + pageSc)
                        Log.e(TAG,"getSubCategories -> error message = ${it.toString()}")

                        if (pageSc == 1) {
                            if (it.subcategories.size == 0)
                                view.showEmptyViewForList()
                            else
                                view.finishLoading()
                        }
                        view.addSubCategories(it.subcategories)
                        /*if (it.pagination.pageCount > 1)
                            view.addLoadingFooter()
                         */
                    }

              /*  run {
                    Log.d(TAG, "page: " + pageSc)
                    Log.e(TAG,"getSubCategories -> error message = ${it.toString()}")

                    if (pageSc == 1) {
                        if (it.subcategories.size == 0)
                            view.showEmptyViewForList()
                        else
                            view.finishLoading()

                        view.addSubCategories(it.subcategories)
                    }
                }*/

            },{
                Log.e(TAG,"getSubCategories -> error message = ${it.message}")
                view.connectionError()
            })
            .also { disposables.add(it) }


    }

    fun testListShouldReplacedWithCallUseCase(): MutableList<CategoryModel>
    {
        val list: MutableList<CategoryModel> = ArrayList()

        var categoryModel = CategoryModel(
            "Cosmetics",
            "1",
            "https://thumbs.dreamstime.com/t/coffee-products-jacobs-douwe-egberts-poznan-poland-oct-brand-marketed-europe-headquartered-amsterdam-netherlands-78946534.jpg"
        )
        list.add(categoryModel)
        categoryModel = CategoryModel(
            "Souvenir",
            "2",
            "http://www.around-amsterdam.com/images/dutch-cheese-1000.jpg"
        )
        list.add(categoryModel)
        categoryModel = CategoryModel(
            "Food",
            "2",
            "https://d2v9y0dukr6mq2.cloudfront.net/video/thumbnail/N_-wj6-wx/videoblocks-netherlands-cheese-wheel-display-in-a-row-famous-diary-product-of-amsterdam_hjl2p34zae_thumbnail-full01.png"
        )
        list.add(categoryModel)
        categoryModel = CategoryModel(
            "Souvenir",
            "2",
            "https://www.cocacolanederland.nl/content/dam/journey/nl/nl/private/stories/2017/2017-vernieuwde-sprite.jpg"
        )
        list.add(categoryModel)
        categoryModel = CategoryModel(
            "Souvenir",
            "2",
            "https://i4.aroq.com/3/2017-07-03-13-44-13094263_1069251323134382_829762458584806910_n_cropped_90.jpg"
        )
        list.add(categoryModel)
        categoryModel = CategoryModel(
            "Food",
            "2",
            "https://cdn-a.william-reed.com/var/wrbm_gb_food_pharma/storage/images/5/1/4/0/1070415-1-eng-GB/Unilever-soup-move-boosts-aseptic-in-Netherlands_wrbm_large.jpg"
        )
        list.add(categoryModel)
        categoryModel = CategoryModel(
            "Souvenir",
            "2",
            "https://cdn-a.william-reed.com/var/wrbm_gb_food_pharma/storage/images/1/9/5/8/528591-1-eng-GB/North-America-s-54bn-packaging-market-in-flux_wrbm_small.jpg"
        )
        list.add(categoryModel)

        return list
    }

    override fun onDestroy(owner: LifecycleOwner) {
        disposables.dispose()
    }

}