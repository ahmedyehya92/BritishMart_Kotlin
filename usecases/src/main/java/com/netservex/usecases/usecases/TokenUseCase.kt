package com.netservex.usecases.usecases

import com.netservex.usecases.repository.Repository
import com.netservex.usecases.repository.repository

class TokenUseCase
{
    fun login(email: String, password: String) = repository.login(email, password)


    fun getTokenInterceptor(refreshToken: String) = repository.getTokenHeader(refreshToken)

    fun getTokenInterceptor(email: String, password: String) = repository.getTokenHeader(email, password)


    var isLoggedIn: Boolean
        get() = repository.getLoggedInStatus()
        set(isLoggedIn) = repository.setLoggedInStatus(isLoggedIn)

    var token: String
        get() = repository.getToken()
        set(token) = repository.setToken(token)

    var userName: String
        get() = repository.getUserName()
        set(userName) = repository.setUserName(userName)

    var password: String
    get() = repository.getPassword()
    set(password) = repository.setPassword(password)

    var refreshToken: String
        get() = repository.getRefreshToken()
        set(refreshToken) = repository.setRefreshToken(refreshToken)
}
