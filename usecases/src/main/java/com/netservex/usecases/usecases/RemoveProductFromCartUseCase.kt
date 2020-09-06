package com.netservex.usecases.usecases

import com.netservex.usecases.repository.repository

class RemoveProductFromCartUseCase {
    operator fun invoke(productId: Int) = repository.removeProductFromCart(productId)
}