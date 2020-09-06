package com.netservex.usecases.usecases

import com.netservex.usecases.repository.repository

class GetMainOfferUseCase ()
{
    operator fun invoke()= repository.getMainOffer()
}