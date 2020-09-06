package com.netservex.usecases.usecases

import com.netservex.usecases.repository.repository

class GetOffersUseCase {
    operator fun invoke(page: Int)= repository.getOffers(page)
}