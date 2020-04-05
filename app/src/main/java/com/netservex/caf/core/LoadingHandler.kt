package com.netservex.caf.core

interface LoadingHandler {
    fun showLoading()
    fun finishLoading()
    fun connectionError()
    fun faildLoading(message: Any)
}