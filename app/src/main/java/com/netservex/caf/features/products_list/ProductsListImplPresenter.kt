package com.netservex.caf.features.products_list

import android.util.Log
import com.netservex.entities.ProductModel
import java.util.ArrayList

class ProductsListImplPresenter (
    private val view: ProductsListView,
    private var pageSc: Int = 0
): ProductsListPresenter {
    override fun getProductsList(page: Int, categoryId: String) {
        this.pageSc = page
        if (page == 1)
            view.showLoading()



        if (pageSc != 1) {
            view.removeLoadingFooter()
            view.setIsLoadingFalse()
            Log.d("", "page not equal 1: ")
        }

        if (pageSc != 1 && testListShouldReplacedWithCallUseCase().size == 0) run {
            view.setLastPageTrue()
            view.removeLoadingFooter()
            Log.d("", "last page")
            Log.d("", "page: " + pageSc)

        }
        else
            run {

                if (pageSc == 1) {
                    if (testListShouldReplacedWithCallUseCase().size == 0)
                        view.showEmptyViewForList()
                    else
                        view.finishLoading()
                }
                view.addProducts(testListShouldReplacedWithCallUseCase())
                /*if (it.pagination.pageCount > 1)
                    view.addLoadingFooter()
                 */
            }

    }

    fun testListShouldReplacedWithCallUseCase(): MutableList<ProductModel>
    {
        val list: MutableList<ProductModel> = ArrayList()

        var productModel = ProductModel(
            "Pepsi",
            "1",
            "https://thumbs.dreamstime.com/t/coffee-products-jacobs-douwe-egberts-poznan-poland-oct-brand-marketed-europe-headquartered-amsterdam-netherlands-78946534.jpg"
        )
        list.add(productModel)
        productModel = ProductModel(
            "Souvenir",
            "2",
            "http://www.around-amsterdam.com/images/dutch-cheese-1000.jpg"
        )
        list.add(productModel)
        productModel = ProductModel(
            "Food",
            "2",
            "https://d2v9y0dukr6mq2.cloudfront.net/video/thumbnail/N_-wj6-wx/videoblocks-netherlands-cheese-wheel-display-in-a-row-famous-diary-product-of-amsterdam_hjl2p34zae_thumbnail-full01.png"
        )
        list.add(productModel)
        productModel = ProductModel(
            "Souvenir",
            "2",
            "https://www.cocacolanederland.nl/content/dam/journey/nl/nl/private/stories/2017/2017-vernieuwde-sprite.jpg"
        )
        list.add(productModel)
        productModel = ProductModel(
            "Souvenir",
            "2",
            "https://i4.aroq.com/3/2017-07-03-13-44-13094263_1069251323134382_829762458584806910_n_cropped_90.jpg"
        )
        list.add(productModel)
        productModel = ProductModel(
            "Food",
            "2",
            "https://cdn-a.william-reed.com/var/wrbm_gb_food_pharma/storage/images/5/1/4/0/1070415-1-eng-GB/Unilever-soup-move-boosts-aseptic-in-Netherlands_wrbm_large.jpg"
        )
        list.add(productModel)
        productModel = ProductModel(
            "Souvenir",
            "2",
            "https://cdn-a.william-reed.com/var/wrbm_gb_food_pharma/storage/images/1/9/5/8/528591-1-eng-GB/North-America-s-54bn-packaging-market-in-flux_wrbm_small.jpg"
        )
        list.add(productModel)

        return list
    }
}