package com.netservex.caf.features.forget_password

import androidx.lifecycle.DefaultLifecycleObserver
import com.netservex.caf.core.LoadingHandler

interface ForgetPasswordView: LoadingHandler {
    fun successfulForgetPassword()
    fun failedForgetPassword()
}

interface ForgetPasswordPresenter: DefaultLifecycleObserver
{
    fun forgetPassword(email: String)
}