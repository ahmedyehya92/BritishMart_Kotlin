package com.netservex.caf.features.subcategories

import androidx.lifecycle.DefaultLifecycleObserver
import com.netservex.caf.core.LoadingHandler
import com.netservex.entities.CategoryModel
import com.netservex.entities.SubCategoryModel

interface SubCategoriesView: LoadingHandler {
    fun addSubCategories(requests: MutableList<SubCategoryModel>)
    fun setLastPageTrue()
    fun addLoadingFooter()
    fun removeLoadingFooter()
    fun showRetryAdapter()
    fun setIsLoadingFalse()
    fun showEmptyViewForList()
}

interface SubCategoriesPresenter: DefaultLifecycleObserver {
    fun getSubCategories(page: Int, categoryId: String)
}