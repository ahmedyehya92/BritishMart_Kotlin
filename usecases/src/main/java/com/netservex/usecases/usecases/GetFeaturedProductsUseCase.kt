package com.netservex.usecases.usecases

import com.netservex.usecases.repository.repository

class GetFeaturedProductsUseCase {
    operator fun invoke(page: Int) = repository.getFeaturedProducts(page)
}