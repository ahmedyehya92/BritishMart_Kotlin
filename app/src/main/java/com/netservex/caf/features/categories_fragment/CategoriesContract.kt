package com.netservex.caf.features.categories_fragment

import android.arch.lifecycle.DefaultLifecycleObserver
import android.content.Context
import com.netservex.entities.CategoryModel
import java.util.*

interface CategoriesView {
    fun showLoading()
    fun finishLoading()
    fun connectionError(message: String? = null)
    fun addMoreToAdapter(list: ArrayList<CategoryModel>)
    fun passContext(): Context
}

interface CategoriesPresenter: DefaultLifecycleObserver {
    fun loadCategories()
}