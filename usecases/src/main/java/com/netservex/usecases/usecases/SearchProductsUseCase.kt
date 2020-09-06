package com.netservex.usecases.usecases

import com.netservex.usecases.repository.repository

class SearchProductsUseCase {
    operator fun invoke(searchQuery: String, page: Int)= repository.searchProducts(searchQuery, page)
}