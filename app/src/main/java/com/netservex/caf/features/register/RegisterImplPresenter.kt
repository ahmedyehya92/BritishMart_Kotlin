package com.netservex.caf.features.register

import androidx.lifecycle.LifecycleOwner
import com.netservex.usecases.usecases.RegisterUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class RegisterImplPresenter(
    private val view: RegisterView,
    private val registerUseCase: RegisterUseCase = RegisterUseCase(),
    private val disposables: CompositeDisposable = CompositeDisposable()
) : RegisterPresenter {

    override fun register(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        passwordConfirmation: String,
        mobileNumber: String
    ) {
        view.showLoading()
        registerUseCase(firstName, lastName, email, password, passwordConfirmation, mobileNumber)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                it.user?.apply {
                    if(this.code != null && this.code == "0")
                        view.connectionError(this.msg)

                    else {
                        view.finishLoading()
                        view.successfulRegister()
                    }
                }

            },{
                view.finishLoading()
                view.connectionError(it.message)
            })
            .also { disposables.add(it) }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        disposables.dispose()
    }

}