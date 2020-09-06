package com.netservex.caf.features.payment_credit

interface CreditPaymentView{
    fun showPaymentLoading()
    fun finishPaymentLoading()
    fun connectionPaymentError(message: String? = null)
    fun onSuccessPayment()
}

interface CreditPaymentPresenter{

}