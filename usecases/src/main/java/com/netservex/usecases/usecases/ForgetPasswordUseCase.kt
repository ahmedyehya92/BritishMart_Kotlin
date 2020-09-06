package com.netservex.usecases.usecases

import com.netservex.usecases.repository.repository

class ForgetPasswordUseCase {
    operator fun invoke(email: String) = repository.forgetPassword(email)
}