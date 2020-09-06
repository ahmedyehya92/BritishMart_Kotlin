package com.netservex.caf.features.forget_password

import com.netservex.caf.features.login.LoginView
import com.netservex.usecases.usecases.ForgetPasswordUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ForgetPasswordImplPresenter (
    private val view: ForgetPasswordView,
    private val forgetPasswordUseCase: ForgetPasswordUseCase = ForgetPasswordUseCase(),
    private val disposables: CompositeDisposable = CompositeDisposable()
): ForgetPasswordPresenter {
    override fun forgetPassword(email: String) {
        view.showLoading()
        forgetPasswordUseCase(email)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.finishLoading()
                if(it.response.msg=="not sent")
                    view.failedForgetPassword()
                else if(it.response.msg=="sent")
                    view.successfulForgetPassword()
            },{
                view.connectionError(it.message)
            })
            .also { disposables.add(it) }
    }
}