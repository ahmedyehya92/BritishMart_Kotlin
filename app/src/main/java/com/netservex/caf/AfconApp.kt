package com.netservex.caf

import android.app.Application
import com.netservex.usecases.Domain

class AfconApp: Application() {
    private lateinit var instance: AfconApp

    override fun onCreate() {
        super.onCreate()
        instance = this
        Domain.integrateWith(this)

    }

    fun getInstance(): AfconApp {
        return instance
    }


}