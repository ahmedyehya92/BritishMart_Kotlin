package com.netservex.caf.core

interface LoadingHandler {
    fun showLoading()
    fun finishLoading()
    fun connectionError(message: String?="")
    fun faildLoading(message: Any)
}