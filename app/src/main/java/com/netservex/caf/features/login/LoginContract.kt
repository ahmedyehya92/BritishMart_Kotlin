package com.netservex.caf.features.login

import androidx.lifecycle.DefaultLifecycleObserver
import com.netservex.caf.core.LoadingHandler

interface LoginView: LoadingHandler {
    fun successfulLogin()
    fun failedLogin()
}

interface LoginPresenter: DefaultLifecycleObserver
{
    fun login(email: String, password: String)
}