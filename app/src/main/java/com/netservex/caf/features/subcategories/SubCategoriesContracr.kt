package com.netservex.caf.features.subcategories

import com.netservex.caf.core.LoadingHandler
import com.netservex.entities.CategoryModel

interface SubCategoriesView: LoadingHandler {
    fun addSubCategories(requests: MutableList<CategoryModel>)
    fun setLastPageTrue()
    fun addLoadingFooter()
    fun removeLoadingFooter()
    fun showRetryAdapter()
    fun setIsLoadingFalse()
    fun showEmptyViewForList()
}

interface SubCategoriesPresenter {
    fun getSubCategories(page: Int, categoryId: String)
}