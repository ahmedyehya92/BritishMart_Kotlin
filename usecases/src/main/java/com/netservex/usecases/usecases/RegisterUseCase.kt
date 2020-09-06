package com.netservex.usecases.usecases

import com.netservex.usecases.repository.repository

class RegisterUseCase {
    operator fun invoke(firstName: String,
                        lastName: String,
                        email: String,
                        password: String,
                        passwordConfirmation: String,
                        mobileNumber: String) = repository.register(firstName, lastName, email, password, passwordConfirmation, mobileNumber)
}