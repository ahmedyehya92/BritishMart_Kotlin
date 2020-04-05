package com.netservex.caf.core

import com.netservex.entities.CategoryModel
import java.util.*

class PassedDataFromCategoriesToMenu(categoriesArrayList: ArrayList<CategoryModel>) {
    private val categoriesArrayList: ArrayList<CategoryModel>
    fun getCategoriesArrayList(): ArrayList<CategoryModel> {
        return categoriesArrayList
    }

    init {
        this.categoriesArrayList = categoriesArrayList
    }
}
