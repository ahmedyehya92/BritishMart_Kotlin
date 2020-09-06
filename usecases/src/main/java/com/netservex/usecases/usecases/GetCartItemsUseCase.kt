package com.netservex.usecases.usecases

import com.netservex.usecases.repository.repository

class GetCartItemsUseCase {
    operator fun invoke()= repository.getCartItems()
}