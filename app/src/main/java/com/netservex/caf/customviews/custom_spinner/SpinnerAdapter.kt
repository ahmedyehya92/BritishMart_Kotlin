package com.netservex.caf.customviews.custom_spinner

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import android.content.Context
import androidx.annotation.StringRes
import android.util.Log

open class SpinnerAdapter<T> (
    lifecycleOwner: LifecycleOwner,
    list: MutableLiveData<MutableList<T>>,
    aContext: Context? = null,
    @StringRes private val  hint: Int
    ): CustomeHintSpinnerAdapter<T>(aContext!!,list.value!!,hint)

{

init {
    list.observe(lifecycleOwner, Observer {
        //notifyDataSetChanged()
        addAll(it)
        Log.i("", it!!.size.toString())
    })

}

}
