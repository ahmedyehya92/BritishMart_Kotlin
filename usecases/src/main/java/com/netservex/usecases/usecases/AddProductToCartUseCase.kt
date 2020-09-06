package com.netservex.usecases.usecases

import com.netservex.usecases.repository.repository

class AddProductToCartUseCase {
    operator fun invoke(productId: Int, quantity: Int) = repository.addProductToCart(productId, quantity)
}