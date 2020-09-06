package com.netservex.usecases.engine

import androidx.lifecycle.MutableLiveData

fun <T> T.toMutableLiveData(): MutableLiveData<T> {
    return MutableLiveData<T>()
        .also { it.value = this }
}