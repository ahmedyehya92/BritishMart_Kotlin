package com.netservex.caf.features.register

import androidx.lifecycle.DefaultLifecycleObserver
import com.netservex.caf.core.LoadingHandler

interface RegisterView: LoadingHandler {
    fun successfulRegister()
    fun failedRegister()
}

interface RegisterPresenter: DefaultLifecycleObserver {
    fun register(firstName: String, lastName: String, email: String, password: String, passwordConfirmation: String, mobileNumber: String)
}