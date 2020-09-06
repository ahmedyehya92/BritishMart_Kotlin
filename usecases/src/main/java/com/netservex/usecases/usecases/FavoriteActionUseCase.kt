package com.netservex.usecases.usecases

import com.netservex.usecases.repository.repository

class FavoriteActionUseCase {
    operator fun invoke(productId: Int)= repository.favoriteAction(productId)
}