package com.netservex.caf.features.categories_fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.netservex.caf.R
import com.netservex.entities.CategoryModel
import java.util.*

class AdapterCategories(
    private val context: Context,
    private val arrayList: ArrayList<CategoryModel>
) :
    androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder?>() {
    private var customListener: CustomButtonListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        var viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder? = null
        val inflater = LayoutInflater.from(parent.context)
        val viewItem: View =
            inflater.inflate(R.layout.view_category_header_item, parent, false)
        viewHolder = CategoriesViewHolder(viewItem)
        return viewHolder
    }

    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        val categoryModel: CategoryModel = arrayList!![position]
        val categoriesViewHolder =
            holder as CategoriesViewHolder
        categoriesViewHolder.tv_title.setText(categoryModel.name)

        categoryModel.image?.apply {
            Glide.with(context)
                .load(categoryModel.image)
                .into(categoriesViewHolder.img_desc)
        }
        categoriesViewHolder.img_desc!!.setImageBitmap(categoryModel.im)
        categoriesViewHolder.lout_container!!.setOnClickListener {
            customListener!!.onItemCategoryClickListner(
                categoryModel.name,
                categoryModel.id
            )
        }
    }

    override fun getItemCount(): Int = arrayList?.size ?: 0

    fun add(r: CategoryModel) {
        arrayList!!.add(r)
        notifyItemInserted(arrayList.size - 1)
    }

    fun addAll(opResults: ArrayList<CategoryModel>) {
        for (result in opResults) {
            add(result)
        }
    }

    inner class CategoriesViewHolder(itemView: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        val img_desc by lazy { itemView.findViewById<ImageView>(R.id.img_desc) }



        val tv_title by lazy { itemView.findViewById<TextView>(R.id.tv_title) }

        val lout_container by lazy { itemView.findViewById<LinearLayout>(R.id.lout_container) }

    }

    interface CustomButtonListener {
        fun onItemCategoryClickListner(title: String?, id: String?)
    }

    fun setCustomButtonListner(listener: CustomButtonListener?) {
        customListener = listener
    }

}