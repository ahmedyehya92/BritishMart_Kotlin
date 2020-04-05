package com.netservex.caf.base

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.netservex.usecases.engine.toMutableLiveData
import com.netservex.usecases.usecases.TokenUseCase

class BaseViewModel (
    private val screenMutableLiveData : MutableLiveData<Int> = (-1).toMutableLiveData(),
    private val tokenUseCase: TokenUseCase = TokenUseCase()
    )
    :ViewModel()
{
    var screenIndicator
        get() = screenMutableLiveData.value
        set(screenValue) = screenMutableLiveData.setValue(screenValue)

    var isLoggedIn
        get() = tokenUseCase.isLoggedIn
        set(value) {tokenUseCase.isLoggedIn = false}
}