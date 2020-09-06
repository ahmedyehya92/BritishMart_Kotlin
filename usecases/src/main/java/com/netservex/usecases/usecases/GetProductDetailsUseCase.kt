package com.netservex.usecases.usecases

import com.netservex.usecases.repository.repository

class GetProductDetailsUseCase {
    operator fun invoke(productId: Int)= repository.getProductDetails(productId)
}