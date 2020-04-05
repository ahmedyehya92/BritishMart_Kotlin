package com.netservex.caf.features.menu_dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.netservex.caf.R
import com.netservex.entities.CategoryModel

class MenuListAdapter(
    context: Context? = null,
    items: MutableList<CategoryModel>
) :
    ArrayAdapter<CategoryModel>(context, 0, items) {
    var viewHolder: ViewHolder? = null
    var customeListener: CustomeListener? = null
    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var convertView = convertView
        val menuItemModel: CategoryModel = getItem(position)
        if (convertView == null) {
            convertView =
                LayoutInflater.from(getContext()).inflate(R.layout.view_menu_item, parent, false)
            viewHolder = ViewHolder(convertView)
            convertView!!.tag = viewHolder
        } else viewHolder = convertView.tag as ViewHolder
        viewHolder!!.tvProductName.text = menuItemModel?.name
        viewHolder!!.loutContainer!!.setOnClickListener {
            customeListener!!.onItemClickListener(
                menuItemModel?.name,
                menuItemModel.id,
                position
            )
        }
        return convertView
    }

    interface CustomeListener {
        fun onItemClickListener(
            title: String?,
            itemId: String?,
            position: Int
        )
    }

    fun setCustomButtonListner(listener: CustomeListener?) {
        customeListener = listener
    }

    inner class ViewHolder(itemView: View) {
        val loutContainer by lazy { itemView.findViewById<LinearLayout>(R.id.lout_cont) }
        val tvProductName by lazy { itemView.findViewById<TextView>(R.id.tv_menu_item_name) }
    }

}
