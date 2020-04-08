package com.netservex.usecases.usecases

import com.netservex.usecases.repository.repository

class GetProductsOfCategoryUseCase {
    operator fun invoke(categoryId: String, page: Int) = repository.getCategoryProducts(categoryId, page)
}