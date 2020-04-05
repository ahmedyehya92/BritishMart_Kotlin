package com.netservex.usecases.usecases

import com.netservex.usecases.repository.repository

class GetCategoriesUseCase {
    operator fun invoke() = repository.getCategories()
}