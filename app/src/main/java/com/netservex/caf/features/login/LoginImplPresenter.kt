package com.netservex.caf.features.login

import android.util.Log
import com.netservex.usecases.usecases.TokenUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LoginImplPresenter (
    private val view: LoginView,
    private val tokenUseCase: TokenUseCase = TokenUseCase(),
    private val disposables: CompositeDisposable = CompositeDisposable()
) : LoginPresenter {
    override fun login(email: String, password: String) {
        view.showLoading()
        tokenUseCase.login(email, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.e("LoginImpl","login response = ${it.user.toString()}")
                it.user?.apply {
                    if(this.code != null && this.code == "0")
                        view.connectionError(this.msg)

                    else if(this.code != null && this.code == "1") {
                        view.finishLoading()
                        tokenUseCase.isLoggedIn = true
                        tokenUseCase.token = it.access_token
                        tokenUseCase.userName = email
                        tokenUseCase.password = password
                        view.successfulLogin()
                    }

                    else if(this.code != null && this.code == "3")
                        view.connectionError(this.msg)

                    else
                        view.connectionError(this.msg?:"Unknown error")

                }

            },{
                view.finishLoading()
                view.connectionError(it.message)
            })
            .also { disposables.add(it) }
    }
}