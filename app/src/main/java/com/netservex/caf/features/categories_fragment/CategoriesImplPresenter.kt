package com.netservex.caf.features.categories_fragment

import androidx.lifecycle.LifecycleOwner
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import com.netservex.caf.R
import com.netservex.entities.CategoryModel
import com.netservex.usecases.usecases.GetCategoriesUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class CategoriesImplPresenter (
    private val view: CategoriesView,
    private val getCategoriesUseCase: GetCategoriesUseCase = GetCategoriesUseCase(),
    private val disposables: CompositeDisposable = CompositeDisposable()
): CategoriesPresenter
{
    override fun loadCategories() {
        view.showLoading()
        getCategoriesUseCase()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.finishLoading()
                view.addMoreToAdapter(it.categories)

            },{
                view.connectionError(it.message)
            })
            .also { disposables.add(it) }


        /*val list: ArrayList<CategoryModel> = ArrayList<CategoryModel>()
        var categoryModel = CategoryModel(
            "Cosmetics",
            "1",
            "",
            BitmapFactory.decodeResource(
                (view.passContext() as AppCompatActivity).resources,
                R.drawable.beauty
            )
        )
        list.add(categoryModel)
        categoryModel = CategoryModel(
            "Souvenir",
            "2",
            "",
            BitmapFactory.decodeResource(
                (view.passContext() as AppCompatActivity).resources,
                R.drawable.box
            )
        )
        list.add(categoryModel)
        categoryModel = CategoryModel(
            "Food",
            "2",
            "",
            BitmapFactory.decodeResource(
                (view.passContext() as AppCompatActivity).resources,
                R.drawable.foodd
            )
        )
        list.add(categoryModel)
        view.addMoreToAdapter(list)*/
    }


    override fun onDestroy(owner: LifecycleOwner) {
        disposables.dispose()
    }

}