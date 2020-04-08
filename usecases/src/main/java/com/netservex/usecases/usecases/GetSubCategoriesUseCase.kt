package com.netservex.usecases.usecases

import com.netservex.usecases.repository.repository

class GetSubCategoriesUseCase {
    operator fun invoke(categoryId: String, page: Int) = repository.getSubCategories(categoryId, page)
}